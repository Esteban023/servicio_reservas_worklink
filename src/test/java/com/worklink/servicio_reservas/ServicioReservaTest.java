package com.worklink.servicio_reservas;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.worklink.servicio_reservas.services.ServicioReserva;
import com.worklink.servicio_reservas.DTOS.ReservaDTO;
import com.worklink.servicio_reservas.Responses.ResultadoOperacion;
import com.worklink.servicio_reservas.enums.EstadoReserva;
import com.worklink.servicio_reservas.model.Reserva;
import com.worklink.servicio_reservas.repository.RepositorioReserva;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Disabled;

@Disabled
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ServicioReservaTest {

    @Autowired
    private ServicioReserva servicioReserva;

    @Autowired
    private RepositorioReserva repositorioReserva;

    @BeforeEach
    void limpiarBaseDatos() {
        repositorioReserva.deleteAll();
    }

    @Test
    void deberiaCrearReservaExitosamente() {

        ReservaDTO reservaDTO = crearReservaDTO();

        ResultadoOperacion<Reserva> resultado = servicioReserva.crearReserva(reservaDTO);

        assertTrue(resultado.isExitoso());
        assertEquals("Reserva creada exitosamente.", resultado.getMensaje());

        Reserva reservaGuardada = resultado.getDatos();

        assertNotNull(reservaGuardada);
        assertNotNull(reservaGuardada.getCodigoReserva());

        List<Reserva> reservas = repositorioReserva.findAll();

        assertEquals(1, reservas.size());
    }

    @Test
    void noDeberiaCrearReservaSiNoEstaPagada() {

        ReservaDTO reservaDTO = crearReservaDTO();
        reservaDTO.setEsPagada(false);

        ResultadoOperacion<Reserva> resultado = servicioReserva.crearReserva(reservaDTO);

        assertFalse(resultado.isExitoso());
        assertEquals("El pago es obligatorio.", resultado.getMensaje());
        assertEquals(400, resultado.getCodigoEstado());

        assertEquals(0, repositorioReserva.findAll().size());
    }

    @Test
    void noDeberiaCrearReservaSiClienteYaTieneReservaEnEseHorario() {

        Reserva reservaExistente = crearEntidadReserva();
        reservaExistente.setClienteId(1L);

        repositorioReserva.save(reservaExistente);

        ReservaDTO nuevaReserva = crearReservaDTO();
        nuevaReserva.setClienteId(1L);

        ResultadoOperacion<Reserva> resultado = servicioReserva.crearReserva(nuevaReserva);

        assertFalse(resultado.isExitoso());
        assertEquals(
            "El cliente ya tiene una reserva en ese rango.",
            resultado.getMensaje()
        );

        assertEquals(1, repositorioReserva.findAll().size());
    }

    @Test
    void noDeberiaCrearReservaSiProveedorYaTieneReservaEnEseHorario() {

        Reserva reservaExistente = crearEntidadReserva();
        reservaExistente.setProveedorId(10L);

        repositorioReserva.save(reservaExistente);

        ReservaDTO nuevaReserva = crearReservaDTO();
        nuevaReserva.setProveedorId(10L);
        nuevaReserva.setClienteId(999L);

        ResultadoOperacion<Reserva> resultado = servicioReserva.crearReserva(nuevaReserva);

        assertFalse(resultado.isExitoso());
        assertEquals(
            "El proveedor ya tiene una reserva en ese rango.",
            resultado.getMensaje()
        );

        assertEquals(1, repositorioReserva.findAll().size());
    }

    @Test
    void deberiaCancelarReservaExitosamente() {

        Reserva reserva = crearEntidadReserva();

        Reserva reservaGuardada = repositorioReserva.save(reserva);

        ResultadoOperacion<ReservaDTO> resultado =
            servicioReserva.cancelarReserva(reservaGuardada.getCodigoReserva());

        assertTrue(resultado.isExitoso());
        assertEquals(
            "Reserva cancelada exitosamente.",
            resultado.getMensaje()
        );

        Optional<Reserva> reservaActualizada =
            repositorioReserva.findById(reservaGuardada.getCodigoReserva());

        assertTrue(reservaActualizada.isPresent());

        assertEquals(
            EstadoReserva.CANCELADA,
            reservaActualizada.get().getEstadoReserva()
        );

        assertEquals(
            "La reserva ha sido cancelada exitosamente",
            reservaActualizada.get().getPoliticaCancelacion()
        );
    }

    @Test
    void noDeberiaCancelarReservaInexistente() {

        ResultadoOperacion<ReservaDTO> resultado =
            servicioReserva.cancelarReserva("CODIGO123");

        assertFalse(resultado.isExitoso());
        assertEquals("Reserva no encontrada.", resultado.getMensaje());
        assertEquals(404, resultado.getCodigoEstado());
    }
/*   
    @Test
    void noDeberiaCancelarReservaYaCancelada() {

        Reserva reserva = crearEntidadReserva();
        reserva.setEstadoReserva(EstadoReserva.CANCELADA);

        Reserva reservaGuardada = repositorioReserva.save(reserva);

        ResultadoOperacion<ReservaDTO> resultado =
            servicioReserva.cancelarReserva(reservaGuardada.getCodigoReserva());

        assertFalse(resultado.isExitoso());

        assertEquals(
            "La reserva ya está cancelada.",
            resultado.getMensaje()
        );
    }

    @Test
    void noDeberiaCancelarReservaCompletada() {

        Reserva reserva = crearEntidadReserva();
        reserva.setEstadoReserva(EstadoReserva.COMPLETADA);

        Reserva reservaGuardada = repositorioReserva.save(reserva);

        ResultadoOperacion<ReservaDTO> resultado =
            servicioReserva.cancelarReserva(reservaGuardada.getCodigoReserva());

        assertFalse(resultado.isExitoso());

        assertEquals(
            "La reserva ya está completada. No se puede cancelar.",
            resultado.getMensaje()
        );
    }
*/
    @Test
    void deberiaObtenerReservasPorCliente() {

        Reserva reserva1 = crearEntidadReserva();
        reserva1.setClienteId(1L);

        Reserva reserva2 = crearEntidadReserva();
        reserva2.setClienteId(1L);
        reserva2.setProveedorId(200L);
        reserva2.setRangoTiempoReservado("14:00-16:00");

        repositorioReserva.save(reserva1);
        repositorioReserva.save(reserva2);

        List<Reserva> reservas =
            servicioReserva.obtenerReservasPorCliente(1L);

        assertEquals(2, reservas.size());
    }

    @Test
    void deberiaObtenerReservasPorProveedor() {

        Reserva reserva1 = crearEntidadReserva();
        reserva1.setProveedorId(50L);

        Reserva reserva2 = crearEntidadReserva();
        reserva2.setProveedorId(50L);
        reserva2.setClienteId(999L);
        reserva2.setRangoTiempoReservado("18:00-20:00");

        repositorioReserva.save(reserva1);
        repositorioReserva.save(reserva2);

        List<Reserva> reservas =
            servicioReserva.obtenerReservasPorProveedor(50L);

        assertEquals(2, reservas.size());
    }

    @Test
    void deberiaActualizarReservaACancelada() {

        Reserva reserva = crearEntidadReserva();

        Reserva reservaGuardada = repositorioReserva.save(reserva);

        Reserva reservaActualizada =
            servicioReserva.actualizarReserva(
                reservaGuardada.getCodigoReserva()
            );

        assertNotNull(reservaActualizada);

        assertEquals(
            EstadoReserva.CANCELADA,
            reservaActualizada.getEstadoReserva()
        );
    }

    @Test
    void actualizarReservaDeberiaRetornarNullSiNoExiste() {

        Reserva resultado =
            servicioReserva.actualizarReserva("NO_EXISTE");

        assertNull(resultado);
    }

    private ReservaDTO crearReservaDTO() {

        ReservaDTO dto = new ReservaDTO();

        dto.setRangoTiempoReservado("09:00-11:00");
        dto.setDuracionServicio(120);
        dto.setProveedorId(10L);
        dto.setClienteId(1L);
        dto.setModalidad("Online");
        dto.setUbicacion("Cali");
        dto.setCategoriaServicio("Tecnología");
        dto.setFechaReserva(LocalDate.now().plusDays(1));
        dto.setTituloServicio("Desarrollo Web");
        dto.setDescripcionServicio("Servicio de desarrollo");
        dto.setPoliticaCancelacion("Cancelable");
        dto.setPrecio(new BigDecimal("100.00"));
        dto.setTotalPagado(new BigDecimal("100.00"));
        dto.setEsPagada(true);

        return dto;
    }

    private Reserva crearEntidadReserva() {

        Reserva reserva = new Reserva();

        reserva.setRangoTiempoReservado("09:00-11:00");
        reserva.setDuracionServicio(120);
        reserva.setProveedorId(10L);
        reserva.setClienteId(1L);
        reserva.setModalidad("Online");
        reserva.setUbicacion("Cali");
        reserva.setCategoriaServicio("Tecnología");
        reserva.setFechaReserva(LocalDate.now().plusDays(1));
        reserva.setTituloServicio("Desarrollo Web");
        reserva.setDescripcionServicio("Servicio de desarrollo");
        reserva.setPoliticaCancelacion("Cancelable");
        reserva.setPrecio(new BigDecimal("100.00"));
        reserva.setTotalPagado(new BigDecimal("100.00"));

        return reserva;
    }
}
