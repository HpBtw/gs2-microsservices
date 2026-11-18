package hpbtw.github.com.ms_quadras.dtos;

import hpbtw.github.com.ms_quadras.entity.OcorrenciaZeladoria;
import hpbtw.github.com.ms_quadras.entity.StatusOcorrencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OcorrenciaZeladoriaResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String fotoUrl;
    private String solicitanteNome;
    private StatusOcorrencia status;
    private LocalDate dataAbertura;
    private LocalDate dataResolucao;
    private QuadraResponseDTO quadra;

    public OcorrenciaZeladoriaResponseDTO(OcorrenciaZeladoria o) {
        id = o.getId();
        titulo = o.getTitulo();
        descricao = o.getDescricao();
        fotoUrl = o.getFotoUrl();
        solicitanteNome = o.getSolicitanteNome();
        status = o.getStatus();
        dataAbertura = o.getDataAbertura();
        dataResolucao = o.getDataResolucao();
        quadra = new QuadraResponseDTO(o.getQuadra());
    }
}