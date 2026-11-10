package ar.edu.utn.solicitudes.repositorio;

import ar.edu.utn.solicitudes.dominio.Solicitud;
import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {

    List<Solicitud> listar();

    Optional<Solicitud> buscarPorId(int id);

    void guardar(Solicitud solicitud);

    int siguienteId();
}
