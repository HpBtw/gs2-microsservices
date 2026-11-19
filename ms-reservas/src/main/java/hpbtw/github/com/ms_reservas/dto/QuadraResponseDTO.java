package hpbtw.github.com.ms_reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuadraResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String endereco;
    private String bairro;
    private Double latitude;
    private Double longitude;
    private String modalidade;
    private Boolean coberta;
    private Boolean iluminacaoNoturna;
    private String status;
}