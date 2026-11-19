package hpbtw.github.com.ms_reservas.dto;

import hpbtw.github.com.ms_reservas.entity.Reserva;
import hpbtw.github.com.ms_reservas.entity.StatusReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReservaResponseDTO {

    private Long id;
    private Long quadraId;
    private String nomeCidadao;
    private String cpfCidadao;
    private LocalDate dataReserva;
    private String codigoQrCode;
    private StatusReserva status;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;

    public ReservaResponseDTO(Reserva r) {
        id = r.getId();
        quadraId = r.getQuadraId();
        nomeCidadao = r.getNomeCidadao();
        cpfCidadao = r.getCpfCidadao();
        dataReserva = r.getDataReserva();
        codigoQrCode = r.getCodigoQrCode();
        status = r.getStatus();
        criadoEm = r.getCriadoEm();
        atualizadoEm = r.getAtualizadoEm();
    }
}