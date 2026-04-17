package com.worklink.servicio_reservas.DTOS;

import java.time.LocalDate;

import com.worklink.servicio_reservas.enums.EstadoReserva;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

public class ReservaDTO {
    
    String idReserva;

    @NotNull(message = "El rango de tiempo reservado no puede ser nulo")
    @NotBlank(message = "El rango de tiempo reservado no puede estar vacío")
    private String rangoTiempoReservado; // Almacena algo como "09:00-11:00"

    @NotNull(message = "La política de cancelación no puede ser nula")
    @NotBlank(message = "La política de cancelación no puede estar vacía")
    String politicaCancelacion;

    @NotNull(message = "La categoría no puede ser nula")
    @NotBlank(message = "La categoría no puede estar vacía")
    String categoriaServicio;

    @NotBlank(message = "La modalidad es obligatoria")
    @Pattern(regexp = "PRESENCIAL|ONLINE", message = "El campo modalidad debe ser PRESENCIAL u ONLINE")
    private String modalidad;

    @NotNull(message = "La ubicación no puede ser nula")
    @NotBlank(message = "La ubicación no puede estar vacía")
    String ubicacion;

    @NotNull(message = "El título del servicio no puede ser nulo")
    @NotBlank(message = "El título del servicio no puede estar vacío")
    String tituloServicio;

    @NotNull(message = "La fecha de reserva no puede ser nula")
    LocalDate fechaReserva;

    @NotNull(message = "La descripción del servicio no puede ser nula")
    @NotBlank(message = "La descripción del servicio no puede estar vacía")
    String descripcionServicio;

    @NotNull(message = "La duración no puede ser nula")
    @Min(value = 30, message = "La duración mínima es de 30 minutos")    
    Integer duracionServicio; //Duración en minutos. Minimo 30 minutos
    
    @NotNull(message = "El precio no puede ser nulo")
    @Digits(integer = 10, fraction = 2)
    BigDecimal precio;

    @NotNull(message = "El total pagado no puede ser nulo")
    @Digits(integer = 10, fraction = 2)
    BigDecimal totalPagado;

    //Llave foránea para identificar al cliente que realiza la reserva.
    @NotNull(message = "El id del cliente no puede ser nulo")
    Long clienteId;

    //Llave foránea para identificar al proveedor que ofrece el servicio.
    @NotNull(message = "El id del proveedor no puede ser nulo")
    Long proveedorId;


    EstadoReserva estadoReserva;


    public ReservaDTO() {
    
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getRangoTiempoReservado() {
        return rangoTiempoReservado;
    }

    public void setRangoTiempoReservado(String rangoTiempoReservado) {
        this.rangoTiempoReservado = rangoTiempoReservado;
    }

    public String getCategoriaServicio() {
        return categoriaServicio;
    }

    public void setCategoriaServicio(String categoriaServicio) {
        this.categoriaServicio = categoriaServicio;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTituloServicio() {
        return tituloServicio;
    }

    public void setTituloServicio(String tituloServicio) {
        this.tituloServicio = tituloServicio;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public Integer getDuracionServicio() {
        return duracionServicio;
    }

    public void setDuracionServicio(Integer duracionServicio) {
        this.duracionServicio = duracionServicio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getPoliticaCancelacion() {
        return politicaCancelacion;
    }

    public void setPoliticaCancelacion(String politicaCancelacion) {
        this.politicaCancelacion = politicaCancelacion;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }


}
