package com.worklink.servicio_reservas.Responses;

import com.worklink.servicio_reservas.DTOS.ReservaDTO;

public class ReservaResponse {

    Boolean exito;
    String mensaje;
    ReservaDTO reservaDTO;

    public ReservaResponse() {
    }

    public ReservaResponse(Boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public ReservaResponse(Boolean exito, String mensaje, ReservaDTO reservaDTO) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.reservaDTO = reservaDTO;
    }

    public Boolean getExito() {
        return exito;
    }

    public void setExito(Boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ReservaDTO getReservaDTO() {
        return reservaDTO;
    }

    public void setReservaDTO(ReservaDTO reservaDTO) {
        this.reservaDTO = reservaDTO;
    }
}


    

