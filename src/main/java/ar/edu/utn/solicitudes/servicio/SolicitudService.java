package ar.edu.utn.solicitudes.servicio;

import ar.edu.utn.solicitudes.dominio.Estado;
import ar.edu.utn.solicitudes.dominio.FiltroSolicitudes;
import ar.edu.utn.solicitudes.dominio.NuevaSolicitud;
import ar.edu.utn.solicitudes.dominio.Responsable;
import ar.edu.utn.solicitudes.dominio.Solicitud;
import ar.edu.utn.solicitudes.dominio.SolicitudNoEncontradaException;
import ar.edu.utn.solicitudes.repositorio.SolicitudRepository;
import java.util.List;
import java.util.stream.Collectors;

/** Casos de uso de las solicitudes académicas. */
public class SolicitudService {

    private final SolicitudRepository repositorio;

    public SolicitudService(SolicitudRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Solicitud registrar(NuevaSolicitud datos) {
        Solicitud solicitud = Solicitud.nueva(repositorio.siguienteId(), datos);
        repositorio.guardar(solicitud);
        return solicitud;
    }

    public Estado cambiarEstado(int id, String nuevoEstado) {
        Solicitud solicitud = obtener(id);
        Estado anterior = solicitud.getEstado();
        solicitud.cambiarEstado(Estado.desde(nuevoEstado));
        repositorio.guardar(solicitud);
        return anterior;
    }

    public Solicitud asignarResponsable(int id, String nombre, String area) {
        Solicitud solicitud = obtener(id);
        solicitud.asignarResponsable(new Responsable(nombre, area));
        repositorio.guardar(solicitud);
        return solicitud;
    }

    public List<Solicitud> buscar(FiltroSolicitudes filtro) {
        return repositorio.listar().stream().filter(filtro::cumple).collect(Collectors.toList());
    }

    public List<Solicitud> listar() {
        return repositorio.listar();
    }

    private Solicitud obtener(int id) {
        return repositorio.buscarPorId(id).orElseThrow(() -> new SolicitudNoEncontradaException(id));
    }
}
