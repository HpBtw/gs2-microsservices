package hpbtw.github.com.ms_reservas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quadra_id", nullable = false)
    private Long quadraId;

    @Column(name = "nome_cidadao", nullable = false, length = 120)
    private String nomeCidadao;

    @Column(name = "cpf_cidadao", nullable = false, length = 14)
    private String cpfCidadao;

    @Column(name = "data_reserva", nullable = false)
    private LocalDate dataReserva;

    @Column(name = "codigo_qr_code", nullable = false, length = 50)
    private String codigoQrCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusReserva status;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDate criadoEm;

    @Column(name = "atualizado_em")
    private LocalDate atualizadoEm;
}