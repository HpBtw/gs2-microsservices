package hpbtw.github.com.ms_reservas.dto;

import hpbtw.github.com.ms_reservas.entity.Reserva;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservaRequestDTO {

    @NotNull(message = "Campo quadraId é requerido")
    private Long quadraId;

    @NotBlank(message = "Campo nome do cidadão é requerido")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
    private String nomeCidadao;

    @NotBlank(message = "Campo CPF do cidadão é requerido")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
    private String cpfCidadao;

    @NotNull(message = "Campo data da reserva é requerido")
    @FutureOrPresent(message = "A data da reserva deve ser hoje ou uma data futura")
    private LocalDate dataReserva;


    public ReservaRequestDTO(Reserva r) {
        quadraId = r.getQuadraId();
        nomeCidadao = r.getNomeCidadao();
        cpfCidadao = r.getCpfCidadao();
        dataReserva = r.getDataReserva();
    }
}