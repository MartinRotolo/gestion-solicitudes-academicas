package ar.edu.utn.solicitudes.servicio;

import static org.junit.Assert.assertEquals;

import ar.edu.utn.solicitudes.dominio.Estado;
import ar.edu.utn.solicitudes.dominio.FiltroSolicitudes;
import ar.edu.utn.solicitudes.dominio.NuevaSolicitud;
import ar.edu.utn.solicitudes.dominio.Solicitud;
import ar.edu.utn.solicitudes.dominio.SolicitudNoEncontradaException;
import ar.edu.utn.solicitudes.repositorio.InMemorySolicitudRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class SolicitudServiceTest {

    private SolicitudService servicio;

    @Before
    public void prepararServicioConRepositorioEnMemoria() {
        servicio = new SolicitudService(new InMemorySolicitudRepository());
    }

    private Solicitud registrar(String tipo, String prioridad) {
        return servicio.registrar(new NuevaSolicitud("100783", "Lucía Fernández", "lucia@example.com", tipo,
                prioridad, "Descripción de prueba suficiente"));
    }

    @Test
    public void asignaNumerosCorrelativos() {
        assertEquals(1, registrar("CERTIFICADO", "ALTA").getId());
        assertEquals(2, registrar("RECLAMO", "BAJA").getId());
    }

    @Test
    public void cambiarEstadoDevuelveElEstadoAnterior() {
        Solicitud solicitud = registrar("CERTIFICADO", "ALTA");
        servicio.asignarResponsable(solicitud.getId(), "Carla Méndez", "alumnos");

        Estado anterior = servicio.cambiarEstado(solicitud.getId(), "EN_PROCESO");

        assertEquals(Estado.PENDIENTE, anterior);
        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
    }

    @Test(expected = SolicitudNoEncontradaException.class)
    public void fallaSiLaSolicitudNoExiste() {
        servicio.cambiarEstado(99, "RESUELTA");
    }

    @Test
    public void buscaAplicandoSoloLosFiltrosCompletados() {
        registrar("CERTIFICADO", "ALTA");
        registrar("RECLAMO", "ALTA");
        registrar("RECLAMO", "BAJA");

        List<Solicitud> reclamos = servicio.buscar(new FiltroSolicitudes("reclamo", "", ""));
        List<Solicitud> altas = servicio.buscar(new FiltroSolicitudes("", "", "ALTA"));
        List<Solicitud> todas = servicio.buscar(new FiltroSolicitudes("", "", ""));

        assertEquals(2, reclamos.size());
        assertEquals(2, altas.size());
        assertEquals(3, todas.size());
    }
}
