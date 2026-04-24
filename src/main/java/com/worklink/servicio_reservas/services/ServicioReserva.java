package com.worklink.servicio_reservas.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.worklink.servicio_reservas.enums.EstadoReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worklink.servicio_reservas.DTOS.ReservaDTO;
import com.worklink.servicio_reservas.Responses.ResultadoOperacion;
import com.worklink.servicio_reservas.mappers.ReservaMapper;
import com.worklink.servicio_reservas.model.Reserva;
import com.worklink.servicio_reservas.repository.RepositorioReserva;

@Service
public class ServicioReserva {
    @Autowired
    private RepositorioReserva repositorioReserva;

    public List<Reserva> obtenerReservasClientePorRangoTiempo(Long clienteId, LocalDate fechaReserva, String rangoTiempoReservado) {
        return repositorioReserva.findReservaClienteByRangoTiempoReservado(clienteId, fechaReserva, rangoTiempoReservado);
    }

    public List<Reserva> obtenerReservasProveedorPorRangoTiempo(Long proveedorId, LocalDate fechaReserva, String rangoTiempoReservado) {
        return repositorioReserva.findReservaProveedorByRangoTiempoReservado(proveedorId, fechaReserva, rangoTiempoReservado);
    }

    public Optional<Reserva> obtenerReservaPorCodigo(String codigoReserva) {
        return repositorioReserva.findById(codigoReserva);
    }

    public ResultadoOperacion<Reserva> crearReserva(ReservaDTO reservaDTO) {

        if (!reservaDTO.esPagada()) {
            return ResultadoOperacion.fallo("El pago es obligatorio.", 400);
        }

        List<Reserva> reservasClienteExistentes = obtenerReservasClientePorRangoTiempo(
            reservaDTO.getClienteId(),
            reservaDTO.getFechaReserva(),
            reservaDTO.getRangoTiempoReservado()
        );
        
        if (!reservasClienteExistentes.isEmpty()) {
            return ResultadoOperacion.fallo("El cliente ya tiene una reserva en ese rango.", 400);
        }

        List<Reserva> reservasProveedorExistentes = obtenerReservasProveedorPorRangoTiempo(
            reservaDTO.getProveedorId(),
            reservaDTO.getFechaReserva(),
            reservaDTO.getRangoTiempoReservado()
        );

        if (!reservasProveedorExistentes.isEmpty()) {
            return ResultadoOperacion.fallo("El proveedor ya tiene una reserva en ese rango.", 400);
        }
            
        Reserva reserva = repositorioReserva.save(ReservaMapper.toEntity(reservaDTO));
        return ResultadoOperacion.exito("Reserva creada exitosamente.", reserva);
    }

    public ResultadoOperacion<ReservaDTO> cancelarReserva(String codigoReserva) {
        Optional<Reserva> reservaEncontrada = repositorioReserva.findById(codigoReserva);

        if (reservaEncontrada.isEmpty()) {
            return ResultadoOperacion.fallo("Reserva no encontrada.", 404);
        }

        Reserva reserva = reservaEncontrada.get();

        if (EstadoReserva.CANCELADA == reserva.getEstadoReserva()) {
            return ResultadoOperacion.fallo("La reserva ya está cancelada.", 400);
        }

        if (EstadoReserva.COMPLETADA == reserva.getEstadoReserva()) {
            return ResultadoOperacion.fallo("La reserva ya está completada. No se puede cancelar.", 400);
        }

        Reserva reservaCancelada = reservaCancelada(reserva);
        return ResultadoOperacion.exito("Reserva cancelada exitosamente.", ReservaMapper.toDTO(reservaCancelada));
    }

    public Reserva actualizarReserva(String codigoReserva) {
        Reserva reservaExistente = repositorioReserva.findById(codigoReserva).orElse(null);
        if (reservaExistente == null) {
            return null; // O lanzar una excepción personalizada
        }

        reservaExistente.setEstadoReserva(EstadoReserva.CANCELADA);
        return repositorioReserva.save(reservaExistente);
    }

    public Reserva reservaCancelada(Reserva reservaExistente) {
        reservaExistente.setEstadoReserva(EstadoReserva.CANCELADA);
        reservaExistente.setPoliticaCancelacion("La reserva ha sido cancelada exitosamente");
        return repositorioReserva.save(reservaExistente);
    }

    public List<Reserva> obtenerReservasPorCliente(Long idCliente) {
        return repositorioReserva.findReservaByClienteId(idCliente);
    }

    public List<Reserva> obtenerReservasPorProveedor(Long idProveedor) {
        return repositorioReserva.findReservaByProveedorId(idProveedor);
    }


}