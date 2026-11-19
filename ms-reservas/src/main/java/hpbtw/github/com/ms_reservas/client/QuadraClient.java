package hpbtw.github.com.ms_reservas.client;

import hpbtw.github.com.ms_reservas.dto.QuadraResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("ms-quadras")
public interface QuadraClient {

    @GetMapping("/api/v1/quadras/{id}")
    QuadraResponseDTO getQuadraById(@PathVariable Long id);

    @PutMapping("/api/v1/quadras/{id}/interditar")
    QuadraResponseDTO interditarQuadra(@PathVariable Long id);
}