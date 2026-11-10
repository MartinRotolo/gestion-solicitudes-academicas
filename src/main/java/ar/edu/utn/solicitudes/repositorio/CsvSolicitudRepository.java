package ar.edu.utn.solicitudes.repositorio;

import ar.edu.utn.solicitudes.dominio.Estado;
import ar.edu.utn.solicitudes.dominio.Estudiante;
import ar.edu.utn.solicitudes.dominio.Prioridad;
import ar.edu.utn.solicitudes.dominio.Responsable;
import ar.edu.utn.solicitudes.dominio.Solicitud;
import ar.edu.utn.solicitudes.dominio.TipoSolicitud;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Persiste las solicitudes en un archivo CSV separado por punto y coma. */
public class CsvSolicitudRepository implements SolicitudRepository {

    private static final String SEPARADOR = ";";

    private final Path archivo;
    private final InMemorySolicitudRepository memoria = new InMemorySolicitudRepository();

    public CsvSolicitudRepository(Path archivo) {
        this.archivo = archivo;
        cargar();
    }

    @Override
    public List<Solicitud> listar() {
        return memoria.listar();
    }

    @Override
    public Optional<Solicitud> buscarPorId(int id) {
        return memoria.buscarPorId(id);
    }

    @Override
    public void guardar(Solicitud solicitud) {
        memoria.guardar(solicitud);
        escribirArchivo();
    }

    @Override
    public int siguienteId() {
        return memoria.siguienteId();
    }

    private void cargar() {
        if (!Files.exists(archivo)) {
            return;
        }
        try {
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                memoria.guardar(aSolicitud(linea));
            }
        } catch (IOException e) {
            throw new PersistenciaException("no se pudo leer el archivo " + archivo, e);
        }
    }

    private void escribirArchivo() {
        List<String> lineas = new ArrayList<>();
        for (Solicitud solicitud : memoria.listar()) {
            lineas.add(aLinea(solicitud));
        }
        try {
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException("no se pudo guardar el archivo " + archivo, e);
        }
    }

    private static String aLinea(Solicitud solicitud) {
        Estudiante estudiante = solicitud.getEstudiante();
        Responsable responsable = solicitud.getResponsable();
        return String.join(SEPARADOR,
                String.valueOf(solicitud.getId()),
                estudiante.getLegajo(),
                estudiante.getNombre(),
                estudiante.getEmail(),
                solicitud.getTipo().name(),
                solicitud.getPrioridad().name(),
                solicitud.getEstado().name(),
                solicitud.getDescripcion(),
                responsable == null ? "" : responsable.getNombre(),
                responsable == null ? "" : responsable.getArea());
    }

    private static Solicitud aSolicitud(String linea) {
        String[] campos = linea.split(SEPARADOR, -1);
        Estudiante estudiante = new Estudiante(campos[1], campos[2], campos[3]);
        Responsable responsable = campos[8].isEmpty() ? null : new Responsable(campos[8], campos[9]);
        return new Solicitud(Integer.parseInt(campos[0]), estudiante, TipoSolicitud.valueOf(campos[4]),
                Prioridad.valueOf(campos[5]), campos[7], Estado.valueOf(campos[6]), responsable);
    }
}
