package hpbtw.github.com.ms_quadras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_quadra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Quadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, length = 200)
    private String endereco;

    @Column(nullable = false, length = 100)
    private String bairro;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ModalidadeEsportiva modalidade;

    @Column(nullable = false)
    private Boolean coberta;

    @Column(name = "iluminacao_noturna", nullable = false)
    private Boolean iluminacaoNoturna;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusQuadra status;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDate criadoEm;

    @Column(name = "atualizado_em")
    private LocalDate atualizadoEm;

    @OneToMany(mappedBy = "quadra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OcorrenciaZeladoria> ocorrencias = new ArrayList<>();

    public boolean isDisponivelParaReserva() {
        return StatusQuadra.DISPONIVEL.equals(this.status);
    }

    public void alterarStatus(StatusQuadra novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("O novo status da quadra não pode ser nulo.");
        }
        this.status = novoStatus;
    }
}