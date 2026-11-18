package hpbtw.github.com.ms_quadras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_ocorrencia_zeladoria")
public class OcorrenciaZeladoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 600)
    private String descricao;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "solicitante_nome", nullable = false, length = 120)
    private String solicitanteNome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOcorrencia status;

    @Column(name = "data_abertura", nullable = false, updatable = false)
    private LocalDate dataAbertura;

    @Column(name = "data_resolucao")
    private LocalDate dataResolucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quadra_id", nullable = false)
    private Quadra quadra;
}