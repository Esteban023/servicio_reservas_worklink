package com.worklink.servicio_reservas.tasks;

import com.worklink.servicio_reservas.enums.EstadoReserva;
import com.worklink.servicio_reservas.model.Reserva;
import com.worklink.servicio_reservas.repository.RepositorioReserva;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class ReservaScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ReservaScheduledTask.class);
    private static final ZoneId ZONA_COT = ZoneId.of("America/Bogota");
    private static final String MENSAJE_NO_REEMBOLSABLE = "Esta reservación no es reembolsable";

    private final RepositorioReserva repositorioReserva;

    public ReservaScheduledTask(RepositorioReserva repositorioReserva) {
        this.repositorioReserva = repositorioReserva;
    }

    /**
     * Se ejecuta diariamente a medianoche para actualizar políticas de cancelación
     * Cambia a "no reembolsable" cuando la fecha de la reserva ya llegó
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void actualizarPolicasVencidas() {
        logger.info("Iniciando actualización de políticas de cancelación vencidas...");

        try {
            LocalDate hoyEnCot = LocalDate.now(ZONA_COT);
            List<Reserva> todasLasReservas = repositorioReserva.findAll();

            int actualizadas = 0;
            for (Reserva reserva : todasLasReservas) {
                if (reserva.getFechaReserva() != null && !hoyEnCot.isBefore(reserva.getFechaReserva())) {
                    if (!MENSAJE_NO_REEMBOLSABLE.equals(reserva.getPoliticaCancelacion())) {
                        reserva.setPoliticaCancelacion(MENSAJE_NO_REEMBOLSABLE);
                        reserva.setEstadoReserva(
                            EstadoReserva.COMPLETADA
                        );
                        repositorioReserva.save(reserva);
                        actualizadas++;
                    }
                }
            }

            logger.info("Actualización completada. {} reservas marcadas como no reembolsables.", actualizadas);
        } catch (Exception e) {
            logger.error("Error al actualizar políticas de cancelación vencidas", e);
        }
    }
}
