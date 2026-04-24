package com.worklink.servicio_reservas.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.worklink.servicio_reservas.enums.EstadoReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worklink.servicio_reservas.DTOS.ReservaDTO;
import com.worklink.servicio_reservas.mappers.ReservaMapper;
import com.worklink.servicio_reservas.model.Reserva;
import com.worklink.servicio_reservas.repository.RepositorioReserva;

@Service
public class ServicioReserva {
    @Autowired
    private RepositorioReserva repositorioReserva;

    public List<Reserva> obtenerReservasoPorRangoTiempo(Long proveedorId, Long clienteId, LocalDate fechaReserva, String rangoTiempoReservado) {
        return repositorioReserva.findReservaByRangoTiempoReservado(proveedorId, clienteId, fechaReserva, rangoTiempoReservado);
    }

    public Optional<Reserva> obtenerReservaPorCodigo(String codigoReserva) {
        return repositorioReserva.findById(codigoReserva);
    }

    public Reserva crearReserva(ReservaDTO reservaDTO) {
        return repositorioReserva.save(
            ReservaMapper.toEntity(reservaDTO)
        );
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