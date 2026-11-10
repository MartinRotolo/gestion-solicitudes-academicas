package ar.edu.utn.solicitudes.servicio;

import static org.junit.Assert.assertEquals;

import ar.edu.utn.solicitudes.dominio.Estado;
import ar.edu.utn.solicitudes.dominio.NuevaSolicitud;
import ar.edu.utn.solicitudes.dominio.Prioridad;
import ar.edu.utn.solicitudes.dominio.TipoSolicitud;
import ar.edu.utn.solicitudes.repositorio.InMemorySolicitudRepository;
import ar.edu.utn.solicitudes.repositorio.SolicitudRepository;
import org.junit.Test;

public class ReporteServiceTest {

    @Test
    public void cuentaPorEstadoPrioridadYTipoEIdentificaUrgentes() {
        SolicitudRepository repositorio = new InMemorySolicitudRepository();
        SolicitudService servicio = new SolicitudService(repositorio);
        servicio.registrar(datos("CERTIFICADO", "ALTA"));
        servicio.registrar(datos("RECLAMO", "ALTA"));
        servicio.registrar(datos("RECLAMO", "BAJA"));
        servicio.cambiarEstado(2, "RECHAZADA");

        Reporte reporte = new ReporteService(repositorio).generar();

        assertEquals(3, reporte.getTotal());
        assertEquals(Long.valueOf(2), reporte.getPorEstado().get(Estado.PENDIENTE));
        assertEquals(Long.valueOf(1), reporte.getPorEstado().get(Estado.RECHAZADA));
        assertEquals(Long.valueOf(0), reporte.getPorEstado().get(Estado.RESUELTA));
        assertEquals(Long.valueOf(2), reporte.getPorPrioridad().get(Prioridad.ALTA));
        assertEquals(Long.valueOf(2), reporte.getPorTipo().get(TipoSolicitud.RECLAMO));
        assertEquals(1, reporte.getUrgentes().size());
        assertEquals(3, reporte.getSinResponsable());
    }

    private NuevaSolicitud datos(String tipo, String prioridad) {
        return new NuevaSolicitud("100783", "Lucía Fernández", "lucia@example.com", tipo, prioridad,
                "Descripción de prueba suficiente");
    }
}
