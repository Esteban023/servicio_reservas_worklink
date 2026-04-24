package com.worklink.servicio_reservas.mappers;

import java.util.List;

import com.worklink.servicio_reservas.DTOS.ReservaDTO;
import com.worklink.servicio_reservas.model.Reserva;

public class ReservaMapper {

    public static Reserva toEntity(ReservaDTO reservaDTO) {
        Reserva reserva = new Reserva();

        reserva.setPrecio(reservaDTO.getPrecio());
        reserva.setRangoTiempoReservado(reservaDTO.getRangoTiempoReservado());
        reserva.setFechaReserva(reservaDTO.getFechaReserva());
        reserva.setCategoriaServicio(reservaDTO.getCategoriaServicio());
        reserva.setProveedorId(reservaDTO.getProveedorId());
        reserva.setClienteId(reservaDTO.getClienteId());
        reserva.setDescripcionServicio(reservaDTO.getDescripcionServicio());
        reserva.setDuracionServicio(reservaDTO.getDuracionServicio());
        reserva.setTituloServicio(reservaDTO.getTituloServicio());
        reserva.setUbicacion(reservaDTO.getUbicacion());
        reserva.setModalidad(reservaDTO.getModalidad());
        reserva.setPoliticaCancelacion(reservaDTO.getPoliticaCancelacion());
        reserva.setTotalPagado(reservaDTO.getTotalPagado());

        return reserva;
    }

    public static ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();

        dto.setIdReserva(reserva.getCodigoReserva());
        dto.setPrecio(reserva.getPrecio());
        dto.setFechaReserva(reserva.getFechaReserva());
        dto.setCategoriaServicio(reserva.getCategoriaServicio());
        dto.setProveedorId(reserva.getProveedorId());
        dto.setClienteId(reserva.getClienteId());
        dto.setDescripcionServicio(reserva.getDescripcionServicio());
        dto.setDuracionServicio(reserva.getDuracionServicio());
        dto.setRangoTiempoReservado(reserva.getRangoTiempoReservado());
        dto.setTituloServicio(reserva.getTituloServicio());
        dto.setUbicacion(reserva.getUbicacion());
        dto.setModalidad(reserva.getModalidad());
        dto.setPoliticaCancelacion(reserva.getPoliticaCancelacion());
        dto.setTotalPagado(reserva.getTotalPagado());
        dto.setEstadoReserva(reserva.getEstadoReserva());

        return dto;
    }

    public static List<ReservaDTO> toDTOList(List<Reserva> reservas) {
        return reservas.stream()
            .map(ReservaMapper::toDTO)
            .toList();
    }
}