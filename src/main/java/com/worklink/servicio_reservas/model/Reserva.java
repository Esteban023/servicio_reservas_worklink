package com.worklink.servicio_reservas.model;

import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import com.worklink.servicio_reservas.enums.EstadoReserva;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @Column(name = "codigo_reserva", unique = true, nullable = false, length = 12)
    private String codigoReserva;

    @Column(name = "rango_tiempo_reservado", nullable = false)
    private String rangoTiempoReservado; // Almacena algo como "09:00-11:00"

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 30, message = "La duración mínima es 30 minutos")
    private Integer duracionServicio;
    
    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "modalidad", nullable = false)
    private String modalidad; //PRESENCIAL u ONLINE

    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;

    @Column(name = "categoria_servicio", nullable = false)
    private String categoriaServicio;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(name = "titulo_servicio", nullable = false)
    private String tituloServicio;

    @Column(name = "descripcion_servicio", length = 500)
    private String descripcionServicio;

    @Column(name = "estado_reserva", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoReserva;

    @Column(name = "politica_cancelacion", length = 500)
    private String politicaCancelacion; //Mensaje que se muestra al cliente sobre la política de cancelación del servicio, si aplica. Puede incluir información sobre reembolsos, plazos para cancelaciones, etc.

    @Column(name = "precio", nullable = false, precision = 12, scale = 2)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal precio;

    @Column(name = "total_pagado", nullable = false, precision = 12, scale = 2)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal totalPagado;

    public Reserva() {

    }
    
    @PrePersist
    private void generarCodigoReservaSiNoExiste() {
        if (codigoReserva == null || codigoReserva.isBlank()) {
            codigoReserva = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();
        }

        this.estadoReserva = EstadoReserva.EN_CURSO;

    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public String getRangoTiempoReservado() {
        return rangoTiempoReservado;
    }

    public void setRangoTiempoReservado(String rangoTiempoReservado) {
        this.rangoTiempoReservado = rangoTiempoReservado;
    }

    public Integer getDuracionServicio() {
        return duracionServicio;
    }

    public void setDuracionServicio(Integer duracionServicio) {
        this.duracionServicio = duracionServicio;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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

    public String getCategoriaServicio() {
        return categoriaServicio;
    }

    public void setCategoriaServicio(String categoriaServicio) {
        this.categoriaServicio = categoriaServicio;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getTituloServicio() {
        return tituloServicio;
    }

    public void setTituloServicio(String tituloServicio) {
        this.tituloServicio = tituloServicio;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public String getPoliticaCancelacion() {
        return politicaCancelacion;
    }

    public void setPoliticaCancelacion(String politicaCancelacion) {
        this.politicaCancelacion = politicaCancelacion;
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


}
