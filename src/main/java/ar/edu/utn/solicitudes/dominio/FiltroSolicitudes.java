package ar.edu.utn.solicitudes.dominio;

/** Criterios de búsqueda. Un criterio vacío no filtra. */
public class FiltroSolicitudes {

    private final String tipo;
    private final String estado;
    private final String prioridad;

    public FiltroSolicitudes(String tipo, String estado, String prioridad) {
        this.tipo = Valores.normalizar(tipo);
        this.estado = Valores.normalizar(estado);
        this.prioridad = Valores.normalizar(prioridad);
    }

    public boolean cumple(Solicitud solicitud) {
        return coincide(tipo, solicitud.getTipo())
                && coincide(estado, solicitud.getEstado())
                && coincide(prioridad, solicitud.getPrioridad());
    }

    private static boolean coincide(String criterio, Enum<?> valor) {
        return criterio.isEmpty() || valor.name().equals(criterio);
    }
}
