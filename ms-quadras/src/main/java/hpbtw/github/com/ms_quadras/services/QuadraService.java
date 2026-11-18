package hpbtw.github.com.ms_quadras.services;

import hpbtw.github.com.ms_quadras.dtos.QuadraRequestDTO;
import hpbtw.github.com.ms_quadras.dtos.QuadraResponseDTO;
import hpbtw.github.com.ms_quadras.entity.Quadra;
import hpbtw.github.com.ms_quadras.entity.StatusQuadra;
import hpbtw.github.com.ms_quadras.exceptions.DatabaseException;
import hpbtw.github.com.ms_quadras.exceptions.ResourceNotFoundException;
import hpbtw.github.com.ms_quadras.repository.QuadraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuadraService {

    @Autowired
    private QuadraRepository quadraRepository;

    @Transactional(readOnly = true)
    public List<QuadraResponseDTO> findAll() {
        List<Quadra> list = quadraRepository.findAll();
        return list.stream().map(QuadraResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public QuadraResponseDTO findById(Long id) {
        Quadra entity = quadraRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Quadra não encontrada. ID: " + id)
        );
        return new QuadraResponseDTO(entity);
    }

    @Transactional
    public QuadraResponseDTO insert(QuadraRequestDTO requestDTO) {
        Quadra entity = new Quadra();
        mapDtoToQuadra(requestDTO, entity);
        entity.setStatus(StatusQuadra.DISPONIVEL);
        entity.setCriadoEm(LocalDate.now());
        entity.setAtualizadoEm(LocalDate.now());
        entity = quadraRepository.save(entity);
        return new QuadraResponseDTO(entity);
    }

    @Transactional
    public QuadraResponseDTO update(Long id, QuadraRequestDTO requestDTO) {
        try {
            Quadra entity = quadraRepository.getReferenceById(id);
            mapDtoToQuadra(requestDTO, entity);
            entity.setAtualizadoEm(LocalDate.now());
            entity = quadraRepository.save(entity);
            return new QuadraResponseDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Quadra não encontrada. ID: " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!quadraRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quadra não encontrada. ID: " + id);
        }
        try {
            quadraRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    @Transactional
    public QuadraResponseDTO disponibilizarQuadra(Long id) {
        Quadra entity = quadraRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Quadra não encontrada. ID: " + id)
        );
        entity.setStatus(StatusQuadra.DISPONIVEL);
        entity.setAtualizadoEm(LocalDate.now());
        entity = quadraRepository.save(entity);
        return new QuadraResponseDTO(entity);
    }

    @Transactional
    public QuadraResponseDTO colocarEmManutencao(Long id) {
        Quadra entity = quadraRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Quadra não encontrada. ID: " + id)
        );
        entity.setStatus(StatusQuadra.EM_MANUTENCAO);
        entity.setAtualizadoEm(LocalDate.now());
        entity = quadraRepository.save(entity);
        return new QuadraResponseDTO(entity);
    }

    @Transactional
    public QuadraResponseDTO interditarQuadra(Long id) {
        Quadra entity = quadraRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Quadra não encontrada. ID: " + id)
        );
        entity.setStatus(StatusQuadra.INTERDITADA);
        entity.setAtualizadoEm(LocalDate.now());
        entity = quadraRepository.save(entity);
        return new QuadraResponseDTO(entity);
    }

    private void mapDtoToQuadra(QuadraRequestDTO dto, Quadra q) {
        q.setNome(dto.getNome());
        q.setDescricao(dto.getDescricao());
        q.setEndereco(dto.getEndereco());
        q.setBairro(dto.getBairro());
        q.setLatitude(dto.getLatitude());
        q.setLongitude(dto.getLongitude());
        q.setModalidade(dto.getModalidade());
        q.setCoberta(dto.getCoberta());
        q.setIluminacaoNoturna(dto.getIluminacaoNoturna());
    }
}