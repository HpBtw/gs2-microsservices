package hpbtw.github.com.ms_reservas.controller;

import hpbtw.github.com.ms_reservas.dto.ReservaRequestDTO;
import hpbtw.github.com.ms_reservas.dto.ReservaResponseDTO;
import hpbtw.github.com.ms_reservas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> getAll() {
        List<ReservaResponseDTO> list = reservaService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> getById(@PathVariable Long id) {
        ReservaResponseDTO responseDTO = reservaService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> insert(@Valid @RequestBody ReservaRequestDTO requestDTO) {
        ReservaResponseDTO responseDTO = reservaService.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(responseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> update(@PathVariable Long id,
                                                     @Valid @RequestBody ReservaRequestDTO requestDTO) {
        ReservaResponseDTO responseDTO = reservaService.update(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/check-in")
    public ResponseEntity<ReservaResponseDTO> realizarCheckIn(@PathVariable Long id) {
        ReservaResponseDTO responseDTO = reservaService.realizarCheckIn(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Long id) {
        ReservaResponseDTO responseDTO = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/cancelar-interdicao")
    public ResponseEntity<ReservaResponseDTO> cancelarPorInterdicaoDaQuadra(@PathVariable Long id) {
        ReservaResponseDTO responseDTO = reservaService.cancelarPorInterdicaoDaQuadra(id);
        return ResponseEntity.ok(responseDTO);
    }
}
