package com.worklink.servicio_reservas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.worklink.servicio_reservas.DTOS.ReservaDTO;
import com.worklink.servicio_reservas.Responses.ReservaResponse;
import com.worklink.servicio_reservas.mappers.ReservaMapper;
import com.worklink.servicio_reservas.model.Reserva;
import com.worklink.servicio_reservas.services.ServicioReserva;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.worklink.servicio_reservas.Responses.ResultadoOperacion;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ServicioReserva servicioReserva;

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        ResultadoOperacion<Reserva> resultado = servicioReserva.crearReserva(reservaDTO);

        if (!resultado.isExitoso()) {
            return ResponseEntity.status(resultado.getCodigoEstado()).body(
                new ReservaResponse(false, resultado.getMensaje())
            );
        }

        return ResponseEntity.ok(new ReservaResponse(true, resultado.getMensaje()));
    }

    @PutMapping("/cancelar_reserva/{codigoReserva}")
    public ResponseEntity<ReservaResponse> cancelarReserva(@PathVariable String codigoReserva) {
        ResultadoOperacion<ReservaDTO> resultado = servicioReserva.cancelarReserva(codigoReserva);

        if (!resultado.isExitoso()) {
            return ResponseEntity.status(resultado.getCodigoEstado()).body(
                new ReservaResponse(false, resultado.getMensaje())
            );
        }

        return ResponseEntity.ok(
            new ReservaResponse(true, resultado.getMensaje(), resultado.getDatos())
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
