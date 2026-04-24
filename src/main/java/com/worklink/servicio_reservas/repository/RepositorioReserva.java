package com.worklink.servicio_reservas.repository;

import com.worklink.servicio_reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface RepositorioReserva extends JpaRepository<Reserva, String> {

    @Query(
        """
        SELECT r FROM Reserva r 
        WHERE r.clienteId = :clienteId 
        AND r.fechaReserva = :fechaReserva 
        AND r.rangoTiempoReservado = :rangoTiempoReservado
        """
    )
    List<Reserva> findReservaClienteByRangoTiempoReservado(
        @Param("clienteId") Long clienteId,
        @Param("fechaReserva") LocalDate fechaReserva,
        @Param("rangoTiempoReservado") String rangoTiempoReservado
    );

    @Query(
        """
        SELECT r FROM Reserva r 
        WHERE r.proveedorId = :proveedorId 
        AND r.fechaReserva = :fechaReserva 
        AND r.rangoTiempoReservado = :rangoTiempoReservado
        """
    )
    List<Reserva> findReservaProveedorByRangoTiempoReservado(
        @Param("proveedorId") Long proveedorId,
        @Param("fechaReserva") LocalDate fechaReserva,
        @Param("rangoTiempoReservado") String rangoTiempoReservado
    );

    @Query(
        """
        SELECT r FROM Reserva r 
        WHERE r.clienteId = :clienteId
        """
    )
    List<Reserva> findReservaByClienteId(@Param("clienteId") Long clienteId);

    @Query(
        """
        SELECT r FROM Reserva r 
        WHERE r.proveedorId = :proveedorId
        """
    )
    List<Reserva> findReservaByProveedorId(@Param("proveedorId") Long proveedorId);




    


}