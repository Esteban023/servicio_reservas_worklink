package com.worklink.servicio_reservas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.worklink.servicio_reservas.DTOS.ReservaDTO;
import com.worklink.servicio_reservas.Responses.ReservaResponse;
import com.worklink.servicio_reservas.enums.EstadoReserva;
import com.worklink.servicio_reservas.mappers.ReservaMapper;
import com.worklink.servicio_reservas.model.Reserva;
import com.worklink.servicio_reservas.services.ServicioReserva;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ServicioReserva servicioReserva;

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@RequestBody ReservaDTO reservaDTO, @RequestParam Boolean pagadBoolean) {

        if (!pagadBoolean) {
            return ResponseEntity.status(400).body(
                new ReservaResponse(false, "El pago de la reserva es obligatorio para crear una reserva.")
            );
        }
        
        List<Reserva> reservasClienteExistentes = servicioReserva.obtenerReservasClientePorRangoTiempo(
            reservaDTO.getClienteId(),
            reservaDTO.getFechaReserva(),
            reservaDTO.getRangoTiempoReservado()
        );
        
        if (!reservasClienteExistentes.isEmpty()) {
            return ResponseEntity.status(409).body(
                new ReservaResponse(false, "Ya existe una reserva en el rango de tiempo especificado.")
            );
        }

        List<Reserva> reservasProveedorExistentes = servicioReserva.obtenerReservasProveedorPorRangoTiempo(
            reservaDTO.getProveedorId(),
            reservaDTO.getFechaReserva(),
            reservaDTO.getRangoTiempoReservado()
        );

        if (!reservasProveedorExistentes.isEmpty()) {
            return ResponseEntity.status(409).body(
                new ReservaResponse(false, "Ya existe una reserva en el rango de tiempo especificado para el proveedor.")
            );
        }

        Reserva reserva = servicioReserva.crearReserva(reservaDTO);
        if (reserva == null) {
            return ResponseEntity.status(500).body(
                new ReservaResponse(false, "Error al crear la reserva.")
            );
        }

        return ResponseEntity.ok(
            new ReservaResponse(true, "Reserva creada exitosamente.")
        );
    }

    @PutMapping("/cancelar_reserva/{codigoReserva}")
    public ResponseEntity<ReservaResponse> cancelarReserva(@PathVariable String codigoReserva) {
        
        Optional<Reserva> reservaEncontrada = servicioReserva.obtenerReservaPorCodigo(codigoReserva);
    
        if (reservaEncontrada.isEmpty()) {
            return ResponseEntity.status(404).body(
                new ReservaResponse(false, "Reserva no encontrada.")
            );
        }

        if (EstadoReserva.CANCELADA == reservaEncontrada.get().getEstadoReserva()) {
            return ResponseEntity.status(400).body(
                new ReservaResponse(false, "La reserva ya está cancelada.")
            );
        }

        if (EstadoReserva.COMPLETADA == reservaEncontrada.get().getEstadoReserva()) {
            return ResponseEntity.status(400).body(
                new ReservaResponse(false, "La reserva ya está completada. No se puede cancelar.")
            );
        }

        Reserva reservaCancelada = servicioReserva.reservaCancelada(
            reservaEncontrada.get()
        );

        return ResponseEntity.ok(
            new ReservaResponse(true, "Reserva actualizada exitosamente.", ReservaMapper.toDTO(reservaCancelada))
        );
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservaResponse> obtenerReservaPorId(@PathVariable String idReserva) {
        Optional<Reserva> reserva = servicioReserva.obtenerReservaPorCodigo(idReserva);

        if (reserva.isEmpty()) {
            return ResponseEntity.status(404).body(
                new ReservaResponse(false, "Reserva no encontrada.")
            );
        }

        return ResponseEntity.ok(
            new ReservaResponse(true, "Reserva encontrada exitosamente.", ReservaMapper.toDTO(reserva.get()))
        );
    }

    @GetMapping("misReservasCliente/{idCliente}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorCliente(@PathVariable Long idCliente) {
        List<Reserva> reservas = servicioReserva.obtenerReservasPorCliente(idCliente);
        List<ReservaDTO> reservasDTO = ReservaMapper.toDTOList(reservas);

        return ResponseEntity.ok(reservasDTO);
    }

    @GetMapping("misReservasProveedor/{idProveedor}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorProveedor(@PathVariable Long idProveedor) {
        List<Reserva> reservas = servicioReserva.obtenerReservasPorProveedor(idProveedor);
        List<ReservaDTO> reservasDTO = ReservaMapper.toDTOList(reservas);

        return ResponseEntity.ok(reservasDTO);
    }
    
    
}
