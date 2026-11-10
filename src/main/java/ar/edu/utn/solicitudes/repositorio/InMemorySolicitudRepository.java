package ar.edu.utn.solicitudes.repositorio;

import ar.edu.utn.solicitudes.dominio.Solicitud;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySolicitudRepository implements SolicitudRepository {

    private final Map<Integer, Solicitud> solicitudes = new LinkedHashMap<>();

    @Override
    public List<Solicitud> listar() {
        return new ArrayList<>(solicitudes.values());
    }

    @Override
    public Optional<Solicitud> buscarPorId(int id) {
        return Optional.ofNullable(solicitudes.get(id));
    }

    @Override
    public void guardar(Solicitud solicitud) {
        solicitudes.put(solicitud.getId(), solicitud);
    }

    @Override
    public int siguienteId() {
        return solicitudes.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }
}
