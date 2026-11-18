package hpbtw.github.com.ms_quadras.controller;

import hpbtw.github.com.ms_quadras.dtos.OcorrenciaZeladoriaRequestDTO;
import hpbtw.github.com.ms_quadras.dtos.OcorrenciaZeladoriaResponseDTO;
import hpbtw.github.com.ms_quadras.services.OcorrenciaZeladoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ocorrencias")
public class OcorrenciaZeladoriaController {

    @Autowired
    private OcorrenciaZeladoriaService ocorrenciaService;

    @GetMapping
    public ResponseEntity<List<OcorrenciaZeladoriaResponseDTO>> getAll() {
        List<OcorrenciaZeladoriaResponseDTO> list = ocorrenciaService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaZeladoriaResponseDTO> getById(@PathVariable Long id) {
        OcorrenciaZeladoriaResponseDTO responseDTO = ocorrenciaService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<OcorrenciaZeladoriaResponseDTO> insert(@Valid @RequestBody OcorrenciaZeladoriaRequestDTO requestDTO) {
        OcorrenciaZeladoriaResponseDTO responseDTO = ocorrenciaService.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(responseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaZeladoriaResponseDTO> update(@PathVariable Long id,
                                                                 @Valid @RequestBody OcorrenciaZeladoriaRequestDTO requestDTO) {
        OcorrenciaZeladoriaResponseDTO responseDTO = ocorrenciaService.update(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ocorrenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/analise")
    public ResponseEntity<OcorrenciaZeladoriaResponseDTO> iniciarAnalise(@PathVariable Long id) {
        OcorrenciaZeladoriaResponseDTO responseDTO = ocorrenciaService.iniciarAnalise(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/resolver")
    public ResponseEntity<OcorrenciaZeladoriaResponseDTO> concluirResolucao(@PathVariable Long id) {
        OcorrenciaZeladoriaResponseDTO responseDTO = ocorrenciaService.concluirResolucao(id);
        return ResponseEntity.ok(responseDTO);
    }
}