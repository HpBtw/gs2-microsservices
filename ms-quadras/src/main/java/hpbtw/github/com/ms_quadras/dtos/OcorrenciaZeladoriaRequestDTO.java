package hpbtw.github.com.ms_quadras.dtos;

import hpbtw.github.com.ms_quadras.entity.OcorrenciaZeladoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OcorrenciaZeladoriaRequestDTO {

    @NotBlank(message = "Campo título é requerido")
    @Size(min = 3, max = 120, message = "O título deve ter entre 3 e 120 caracteres")
    private String titulo;

    @NotBlank(message = "Campo descrição é requerido")
    @Size(min = 10, max = 600, message = "A descrição deve ter entre 10 e 600 caracteres")
    private String descricao;

    @Size(max = 500, message = "A URL da foto deve ter no máximo 500 caracteres")
    private String fotoUrl;

    @NotBlank(message = "Campo nome do solicitante é requerido")
    @Size(min = 3, max = 120, message = "O nome do solicitante deve ter entre 3 e 120 caracteres")
    private String solicitanteNome;

    @NotNull(message = "Campo quadraId é requerido")
    private Long quadraId;

    public OcorrenciaZeladoriaRequestDTO(OcorrenciaZeladoria o) {
        titulo = o.getTitulo();
        descricao = o.getDescricao();
        fotoUrl = o.getFotoUrl();
        solicitanteNome = o.getSolicitanteNome();
        quadraId = o.getQuadra().getId();
    }
}