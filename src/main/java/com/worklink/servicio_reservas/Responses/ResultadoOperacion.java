package com.worklink.servicio_reservas.Responses;

public class ResultadoOperacion<T> {
    private final boolean exitoso;
    private final String mensaje;
    private final T datos;
    private final int codigoEstado;

    private ResultadoOperacion(boolean exitoso, String mensaje, T datos, int codigoEstado) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.datos = datos;
        this.codigoEstado = codigoEstado;
    }

    public static <T> ResultadoOperacion<T> exito(String mensaje, T datos) {
        return new ResultadoOperacion<>(true, mensaje, datos, 200);
    }

    public static <T> ResultadoOperacion<T> fallo(String mensaje, int codigoEstado) {
        return new ResultadoOperacion<>(false, mensaje, null, codigoEstado);
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public T getDatos() {
        return datos;
    }

    public int getCodigoEstado() {
        return codigoEstado;
    }

    
}