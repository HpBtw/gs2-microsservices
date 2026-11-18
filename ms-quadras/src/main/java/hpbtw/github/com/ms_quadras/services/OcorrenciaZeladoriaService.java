package hpbtw.github.com.ms_quadras.services;

import hpbtw.github.com.ms_quadras.dtos.OcorrenciaZeladoriaRequestDTO;
import hpbtw.github.com.ms_quadras.dtos.OcorrenciaZeladoriaResponseDTO;
import hpbtw.github.com.ms_quadras.entity.OcorrenciaZeladoria;
import hpbtw.github.com.ms_quadras.entity.Quadra;
import hpbtw.github.com.ms_quadras.entity.StatusOcorrencia;
import hpbtw.github.com.ms_quadras.exceptions.DatabaseException;
import hpbtw.github.com.ms_quadras.exceptions.ResourceNotFoundException;
import hpbtw.github.com.ms_quadras.repository.OcorrenciaZeladoriaRepository;
import hpbtw.github.com.ms_quadras.repository.QuadraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OcorrenciaZeladoriaService {

    @Autowired
    private OcorrenciaZeladoriaRepository ocorrenciaRepository;

    @Autowired
    private QuadraRepository quadraRepository;

    @Transactional(readOnly = true)
    public List<OcorrenciaZeladoriaResponseDTO> findAll() {
        List<OcorrenciaZeladoria> list = ocorrenciaRepository.findAll();
        return list.stream().map(OcorrenciaZeladoriaResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public OcorrenciaZeladoriaResponseDTO findById(Long id) {
        OcorrenciaZeladoria entity = ocorrenciaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ocorrência de zeladoria não encontrada. ID: " + id)
        );
        return new OcorrenciaZeladoriaResponseDTO(entity);
    }


    @Transactional
    public OcorrenciaZeladoriaResponseDTO insert(OcorrenciaZeladoriaRequestDTO requestDTO) {
        Quadra quadra = quadraRepository.findById(requestDTO.getQuadraId()).orElseThrow(
                () -> new ResourceNotFoundException("Quadra não encontrada. ID: " + requestDTO.getQuadraId())
        );

        OcorrenciaZeladoria entity = new OcorrenciaZeladoria();
        mapDtoToOcorrencia(requestDTO, entity, quadra);
        entity.setStatus(StatusOcorrencia.ABERTA);
        entity.setDataAbertura(LocalDate.now());
        entity = ocorrenciaRepository.save(entity);
        return new OcorrenciaZeladoriaResponseDTO(entity);
    }

    @Transactional
    public OcorrenciaZeladoriaResponseDTO update(Long id, OcorrenciaZeladoriaRequestDTO requestDTO) {
        try {
            OcorrenciaZeladoria o = ocorrenciaRepository.getReferenceById(id);

            if (StatusOcorrencia.RESOLVIDA.equals(o.getStatus())) {
                throw new IllegalArgumentException(
                        String.format("Ocorrência id %d já está RESOLVIDA e não pode ser alterada", id)
                );
            }

            Quadra quadra = quadraRepository.findById(requestDTO.getQuadraId()).orElseThrow(
                    () -> new ResourceNotFoundException("Quadra não encontrada. ID: " + requestDTO.getQuadraId())
            );

            mapDtoToOcorrencia(requestDTO, o, quadra);
            o = ocorrenciaRepository.save(o);
            return new OcorrenciaZeladoriaResponseDTO(o);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Ocorrência de zeladoria não encontrada. ID: " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!ocorrenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ocorrência de zeladoria não encontrada. ID: " + id);
        }
        try {
            ocorrenciaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    @Transactional
    public OcorrenciaZeladoriaResponseDTO iniciarAnalise(Long id) {
        OcorrenciaZeladoria entity = ocorrenciaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ocorrência de zeladoria não encontrada. ID: " + id)
        );

        if (StatusOcorrencia.RESOLVIDA.equals(entity.getStatus())) {
            throw new IllegalArgumentException(
                    String.format("Ocorrência id %d já está RESOLVIDA e não pode voltar para análise", id)
            );
        }

        entity.setStatus(StatusOcorrencia.EM_ANALISE);
        entity = ocorrenciaRepository.save(entity);
        return new OcorrenciaZeladoriaResponseDTO(entity);
    }

    @Transactional
    public OcorrenciaZeladoriaResponseDTO concluirResolucao(Long id) {
        OcorrenciaZeladoria entity = ocorrenciaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ocorrência de zeladoria não encontrada. ID: " + id)
        );

        entity.setStatus(StatusOcorrencia.RESOLVIDA);
        entity.setDataResolucao(LocalDate.now());
        entity = ocorrenciaRepository.save(entity);
        return new OcorrenciaZeladoriaResponseDTO(entity);
    }

    private void mapDtoToOcorrencia(OcorrenciaZeladoriaRequestDTO dto, OcorrenciaZeladoria entity, Quadra quadra) {
        entity.setTitulo(dto.getTitulo());
        entity.setDescricao(dto.getDescricao());
        entity.setFotoUrl(dto.getFotoUrl());
        entity.setSolicitanteNome(dto.getSolicitanteNome());
        entity.setQuadra(quadra);
    }
}
