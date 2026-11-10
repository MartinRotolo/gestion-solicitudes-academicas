package ar.edu.utn.solicitudes.repositorio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ar.edu.utn.solicitudes.dominio.Estado;
import ar.edu.utn.solicitudes.dominio.NuevaSolicitud;
import ar.edu.utn.solicitudes.dominio.Responsable;
import ar.edu.utn.solicitudes.dominio.Solicitud;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;

public class CsvSolicitudRepositoryTest {

    @Test
    public void lasSolicitudesGuardadasSeRecuperanAlVolverACargarElArchivo() throws Exception {
        Path archivo = Files.createTempFile("solicitudes", ".csv");
        Files.delete(archivo);
        CsvSolicitudRepository repositorio = new CsvSolicitudRepository(archivo);
        Solicitud solicitud = Solicitud.nueva(repositorio.siguienteId(), new NuevaSolicitud("100783",
                "Lucía Fernández", "lucia@example.com", "CERTIFICADO", "ALTA", "Certificado de alumno regular"));
        solicitud.asignarResponsable(new Responsable("Carla Méndez", "alumnos"));
        repositorio.guardar(solicitud);

        CsvSolicitudRepository recargado = new CsvSolicitudRepository(archivo);

        Solicitud recuperada = recargado.buscarPorId(1).orElseThrow(AssertionError::new);
        assertEquals("Lucía Fernández", recuperada.getEstudiante().getNombre());
        assertEquals(Estado.PENDIENTE, recuperada.getEstado());
        assertEquals("ALUMNOS", recuperada.getResponsable().getArea());
        assertEquals(2, recargado.siguienteId());
    }

    @Test
    public void siElArchivoNoExisteEmpiezaVacio() throws Exception {
        Path archivo = Files.createTempFile("solicitudes", ".csv");
        Files.delete(archivo);

        CsvSolicitudRepository repositorio = new CsvSolicitudRepository(archivo);

        assertTrue(repositorio.listar().isEmpty());
        assertFalse(Files.exists(archivo));
    }
}
