package hpbtw.github.com.ms_reservas.repository;

import hpbtw.github.com.ms_reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
