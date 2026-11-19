package hpbtw.github.com.ms_reservas.service;

import feign.FeignException;
import hpbtw.github.com.ms_quadras.client.QuadraClient;
import hpbtw.github.com.ms_quadras.dtos.QuadraResponseDTO;
import hpbtw.github.com.ms_reservas.dto.ReservaRequestDTO;
import hpbtw.github.com.ms_reservas.dto.ReservaResponseDTO;
import hpbtw.github.com.ms_reservas.entity.Reserva;
import hpbtw.github.com.ms_reservas.entity.StatusReserva;
import hpbtw.github.com.ms_reservas.exceptions.DatabaseException;
import hpbtw.github.com.ms_reservas.exceptions.ResourceNotFoundException;
import hpbtw.github.com.ms_reservas.repository.ReservaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private QuadraClient quadraClient;

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> getAll() {
        List<Reserva> list = reservaRepository.findAll();
        return list.stream().map(ReservaResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ReservaResponseDTO getById(Long id) {
        Reserva entity = reservaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Reserva não encontrada. ID: " + id)
        );
        return new ReservaResponseDTO(entity);
    }

    @Transactional
    @CircuitBreaker(name = "verificarDisponibilidadeQuadra", fallbackMethod = "insertFallback")
    public ReservaResponseDTO insert(ReservaRequestDTO requestDTO) {
        QuadraResponseDTO quadra = buscarQuadraNoMsQuadras(requestDTO.getQuadraId());
        if (!"DISPONIVEL".equals(quadra.getStatus())) {
            throw new ResourceNotFoundException(
                    "Quadra id " + requestDTO.getQuadraId() + " não está DISPONIVEL. Status atual: " + quadra.getStatus()
            );
        }

        Reserva entity = new Reserva();
        mapDtoToEntity(requestDTO, entity);
        entity.setCodigoQrCode("QR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        entity.setStatus(StatusReserva.CONFIRMADA);
        entity.setCriadoEm(LocalDate.now());
        entity.setAtualizadoEm(LocalDate.now());

        entity = reservaRepository.save(entity);
        return new ReservaResponseDTO(entity);
    }

    public ReservaResponseDTO insertFallback(ReservaRequestDTO requestDTO, Throwable e) {
        if (e instanceof ResourceNotFoundException) {
            throw (RuntimeException) e;
        }
        throw new ResourceNotFoundException(
                "Serviço de quadras indisponível no momento. Não foi possível validar a quadra id " + requestDTO.getQuadraId()
        );
    }

    @Transactional
    public ReservaResponseDTO update(Long id, ReservaRequestDTO requestDTO) {
        try {
            Reserva r = reservaRepository.getReferenceById(id);

            if (!StatusReserva.CONFIRMADA.equals(r.getStatus())) {
                throw new IllegalArgumentException(
                        String.format("Reserva id %d com status %s não pode ser alterada", id, r.getStatus())
                );
            }
            mapDtoToEntity(requestDTO, r);
            r.setAtualizadoEm(LocalDate.now());
            r = reservaRepository.save(r);
            return new ReservaResponseDTO(r);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Reserva não encontrada. ID: " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva não encontrada. ID: " + id);
        }
        try {
            reservaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    @Transactional
    public ReservaResponseDTO realizarCheckIn(Long id) {
        Reserva entity = reservaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Reserva não encontrada. ID: " + id)
        );

        if (!StatusReserva.CONFIRMADA.equals(entity.getStatus())) {
            throw new IllegalArgumentException(
                    String.format("Não é possível realizar check-in na reserva id %d com status %s", id, entity.getStatus())
            );
        }

        entity.setStatus(StatusReserva.CHECKIN_REALIZADO);
        entity.setAtualizadoEm(LocalDate.now());
        entity = reservaRepository.save(entity);
        return new ReservaResponseDTO(entity);
    }

    @Transactional
    public ReservaResponseDTO cancelarReserva(Long id) {
        Reserva entity = reservaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Reserva não encontrada. ID: " + id)
        );

        if (StatusReserva.CHECKIN_REALIZADO.equals(entity.getStatus())) {
            throw new IllegalArgumentException(
                    String.format("Reserva id %d já teve check-in realizado e não pode ser cancelada", id)
            );
        }

        entity.setStatus(StatusReserva.CANCELADA);
        entity.setAtualizadoEm(LocalDate.now());
        entity = reservaRepository.save(entity);
        return new ReservaResponseDTO(entity);
    }

    @Transactional
    @CircuitBreaker(name = "interditarQuadraRemota", fallbackMethod = "cancelarPorInterdicaoFallback")
    public ReservaResponseDTO cancelarPorInterdicaoDaQuadra(Long id) {
        Reserva entity = reservaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Reserva não encontrada. ID: " + id)
        );

        quadraClient.interditarQuadra(entity.getQuadraId());

        entity.setStatus(StatusReserva.CANCELADA);
        entity.setAtualizadoEm(LocalDate.now());
        entity = reservaRepository.save(entity);
        return new ReservaResponseDTO(entity);
    }

    public ReservaResponseDTO cancelarPorInterdicaoFallback(Long id, Throwable e) {
        if (e instanceof ResourceNotFoundException) {
            throw (RuntimeException) e;
        }
        throw new IllegalArgumentException(
                "Serviço de quadras indisponível no momento. Não foi possível interditar a quadra da reserva id " + id
        );
    }

    private QuadraResponseDTO buscarQuadraNoMsQuadras(Long quadraId) {
        try {
            return quadraClient.getQuadraById(quadraId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Quadra não encontrada no ms-quadras. ID: " + quadraId);
        }
    }




    private void mapDtoToEntity(ReservaRequestDTO dto, Reserva r) {
        r.setQuadraId(dto.getQuadraId());
        r.setNomeCidadao(dto.getNomeCidadao());
        r.setCpfCidadao(dto.getCpfCidadao());
        r.setDataReserva(dto.getDataReserva());
    }
}