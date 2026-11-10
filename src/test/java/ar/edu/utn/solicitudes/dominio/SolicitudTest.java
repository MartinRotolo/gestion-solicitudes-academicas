package ar.edu.utn.solicitudes.dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SolicitudTest {

    private Solicitud nuevaSolicitud(String prioridad) {
        NuevaSolicitud datos = new NuevaSolicitud("100783", "Lucía Fernández", "lucia@alumnos.example.com",
                "CERTIFICADO", prioridad, "Certificado de alumno regular");
        return Solicitud.nueva(1, datos);
    }

    @Test
    public void unaSolicitudNuevaQuedaPendienteYSinResponsable() {
        Solicitud solicitud = nuevaSolicitud("ALTA");

        assertEquals(Estado.PENDIENTE, solicitud.getEstado());
        assertFalse(solicitud.tieneResponsable());
    }

    @Test
    public void conResponsablePuedePasarDePendienteAEnProceso() {
        Solicitud solicitud = nuevaSolicitud("ALTA");
        solicitud.asignarResponsable(new Responsable("Carla Méndez", "alumnos"));

        solicitud.cambiarEstado(Estado.EN_PROCESO);

        assertEquals(Estado.EN_PROCESO, solicitud.getEstado());
    }

    @Test
    public void sinResponsableNoPuedePasarAEnProceso() {
        Solicitud solicitud = nuevaSolicitud("ALTA");

        try {
            solicitud.cambiarEstado(Estado.EN_PROCESO);
            fail("Se esperaba ReglaNegocioException");
        } catch (ReglaNegocioException e) {
            assertEquals("la solicitud debe tener un responsable asignado", e.getMessage());
        }
        assertEquals(Estado.PENDIENTE, solicitud.getEstado());
    }

    @Test
    public void unaSolicitudCerradaNoCambiaDeEstado() {
        Solicitud solicitud = nuevaSolicitud("ALTA");
        solicitud.cambiarEstado(Estado.RECHAZADA);

        try {
            solicitud.cambiarEstado(Estado.PENDIENTE);
            fail("Se esperaba ReglaNegocioException");
        } catch (ReglaNegocioException e) {
            assertEquals("no se puede pasar de RECHAZADA a PENDIENTE", e.getMessage());
        }
    }

    @Test(expected = ReglaNegocioException.class)
    public void noSePuedeAsignarResponsableAUnaSolicitudCerrada() {
        Solicitud solicitud = nuevaSolicitud("BAJA");
        solicitud.cambiarEstado(Estado.RECHAZADA);

        solicitud.asignarResponsable(new Responsable("Pablo Sosa", "bedelia"));
    }

    @Test
    public void esUrgenteSiEsDePrioridadAltaYNoEstaCerrada() {
        Solicitud alta = nuevaSolicitud("ALTA");
        Solicitud media = nuevaSolicitud("MEDIA");

        assertTrue(alta.esUrgente());
        assertFalse(media.esUrgente());

        alta.cambiarEstado(Estado.RECHAZADA);
        assertFalse(alta.esUrgente());
    }

    @Test(expected = ReglaNegocioException.class)
    public void rechazaDescripcionesDeMenosDeDiezCaracteres() {
        new Solicitud(1, new Estudiante("100783", "Lucía", "lucia@example.com"), TipoSolicitud.RECLAMO,
                Prioridad.BAJA, "corta", Estado.PENDIENTE, null);
    }
}
