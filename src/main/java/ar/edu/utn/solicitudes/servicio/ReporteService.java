package ar.edu.utn.solicitudes.servicio;

import ar.edu.utn.solicitudes.dominio.Estado;
import ar.edu.utn.solicitudes.dominio.Prioridad;
import ar.edu.utn.solicitudes.dominio.Solicitud;
import ar.edu.utn.solicitudes.dominio.TipoSolicitud;
import ar.edu.utn.solicitudes.repositorio.SolicitudRepository;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReporteService {

    private final SolicitudRepository repositorio;

    public ReporteService(SolicitudRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Reporte generar() {
        List<Solicitud> solicitudes = repositorio.listar();
        return new Reporte(
                solicitudes.size(),
                contarPor(solicitudes, Estado.class, Solicitud::getEstado),
                contarPor(solicitudes, Prioridad.class, Solicitud::getPrioridad),
                contarPor(solicitudes, TipoSolicitud.class, Solicitud::getTipo),
                urgentes(solicitudes),
                contarSinResponsable(solicitudes));
    }

    private static <E extends Enum<E>> Map<E, Long> contarPor(List<Solicitud> solicitudes, Class<E> tipo,
            Function<Solicitud, E> criterio) {
        Map<E, Long> conteo = new EnumMap<>(tipo);
        for (E valor : tipo.getEnumConstants()) {
            conteo.put(valor, 0L);
        }
        for (Solicitud solicitud : solicitudes) {
            conteo.merge(criterio.apply(solicitud), 1L, Long::sum);
        }
        return conteo;
    }

    private static List<Solicitud> urgentes(List<Solicitud> solicitudes) {
        return solicitudes.stream().filter(Solicitud::esUrgente).collect(Collectors.toList());
    }

    private static long contarSinResponsable(List<Solicitud> solicitudes) {
        return solicitudes.stream().filter(solicitud -> !solicitud.tieneResponsable()).count();
    }
}
