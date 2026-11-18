package hpbtw.github.com.ms_quadras.controller;

import hpbtw.github.com.ms_quadras.dtos.QuadraRequestDTO;
import hpbtw.github.com.ms_quadras.dtos.QuadraResponseDTO;
import hpbtw.github.com.ms_quadras.services.QuadraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/quadras")
public class QuadraController {

    @Autowired
    private QuadraService quadraService;

    @GetMapping
    public ResponseEntity<List<QuadraResponseDTO>> getAll() {
        List<QuadraResponseDTO> list = quadraService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuadraResponseDTO> getById(@PathVariable Long id) {
        QuadraResponseDTO responseDTO = quadraService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<QuadraResponseDTO> insert(@Valid @RequestBody QuadraRequestDTO requestDTO) {
        QuadraResponseDTO responseDTO = quadraService.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(responseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuadraResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody QuadraRequestDTO requestDTO) {
        QuadraResponseDTO responseDTO = quadraService.update(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quadraService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disponibilizar")
    public ResponseEntity<QuadraResponseDTO> disponibilizarQuadra(@PathVariable Long id) {
        QuadraResponseDTO responseDTO = quadraService.disponibilizarQuadra(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/manutencao")
    public ResponseEntity<QuadraResponseDTO> colocarEmManutencao(@PathVariable Long id) {
        QuadraResponseDTO responseDTO = quadraService.colocarEmManutencao(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/interditar")
    public ResponseEntity<QuadraResponseDTO> interditarQuadra(@PathVariable Long id) {
        QuadraResponseDTO responseDTO = quadraService.interditarQuadra(id);
        return ResponseEntity.ok(responseDTO);
    }
}