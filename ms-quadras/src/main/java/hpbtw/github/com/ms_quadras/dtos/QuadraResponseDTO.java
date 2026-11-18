package hpbtw.github.com.ms_quadras.dtos;

import hpbtw.github.com.ms_quadras.entity.ModalidadeEsportiva;
import hpbtw.github.com.ms_quadras.entity.Quadra;
import hpbtw.github.com.ms_quadras.entity.StatusQuadra;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class QuadraResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String endereco;
    private String bairro;
    private Double latitude;
    private Double longitude;
    private ModalidadeEsportiva modalidade;
    private Boolean coberta;
    private Boolean iluminacaoNoturna;
    private StatusQuadra status;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;

    public QuadraResponseDTO(Quadra q) {
        id = q.getId();
        nome = q.getNome();
        descricao = q.getDescricao();
        endereco = q.getEndereco();
        bairro = q.getBairro();
        latitude = q.getLatitude();
        longitude = q.getLongitude();
        modalidade = q.getModalidade();
        coberta = q.getCoberta();
        iluminacaoNoturna = q.getIluminacaoNoturna();
        status = q.getStatus();
        criadoEm = q.getCriadoEm();
        atualizadoEm = q.getAtualizadoEm();
    }
}