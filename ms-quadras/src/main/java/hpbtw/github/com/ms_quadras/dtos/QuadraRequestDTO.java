package hpbtw.github.com.ms_quadras.dtos;

import hpbtw.github.com.ms_quadras.entity.ModalidadeEsportiva;
import hpbtw.github.com.ms_quadras.entity.Quadra;
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
public class QuadraRequestDTO {

    @NotBlank(message = "Campo nome é requerido")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotBlank(message = "Campo endereço é requerido")
    @Size(min = 5, max = 200, message = "O endereço deve ter entre 5 e 200 caracteres")
    private String endereco;

    @NotBlank(message = "Campo bairro é requerido")
    @Size(min = 2, max = 100, message = "O bairro deve ter entre 2 e 100 caracteres")
    private String bairro;

    @NotNull(message = "Campo latitude é requerido")
    private Double latitude;

    @NotNull(message = "Campo longitude é requerido")
    private Double longitude;

    @NotNull(message = "Campo modalidade é requerido")
    private ModalidadeEsportiva modalidade;

    @NotNull(message = "Campo coberta é requerido")
    private Boolean coberta;

    @NotNull(message = "Campo iluminação noturna é requerido")
    private Boolean iluminacaoNoturna;

    public QuadraRequestDTO(Quadra q) {
        nome = q.getNome();
        descricao = q.getDescricao();
        endereco = q.getEndereco();
        bairro = q.getBairro();
        latitude = q.getLatitude();
        longitude = q.getLongitude();
        modalidade = q.getModalidade();
        coberta = q.getCoberta();
        iluminacaoNoturna = q.getIluminacaoNoturna();
    }
}