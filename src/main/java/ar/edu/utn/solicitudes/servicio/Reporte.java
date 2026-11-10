package ar.edu.utn.solicitudes.servicio;

import ar.edu.utn.solicitudes.dominio.Estado;
import ar.edu.utn.solicitudes.dominio.Prioridad;
import ar.edu.utn.solicitudes.dominio.Solicitud;
import ar.edu.utn.solicitudes.dominio.TipoSolicitud;
import java.util.List;
import java.util.Map;

/** Resultado del reporte: solo datos, sin formato de presentación. */
public class Reporte {

    private final int total;
    private final Map<Estado, Long> porEstado;
    private final Map<Prioridad, Long> porPrioridad;
    private final Map<TipoSolicitud, Long> porTipo;
    private final List<Solicitud> urgentes;
    private final long sinResponsable;

    public Reporte(int total, Map<Estado, Long> porEstado, Map<Prioridad, Long> porPrioridad,
            Map<TipoSolicitud, Long> porTipo, List<Solicitud> urgentes, long sinResponsable) {
        this.total = total;
        this.porEstado = porEstado;
        this.porPrioridad = porPrioridad;
        this.porTipo = porTipo;
        this.urgentes = urgentes;
        this.sinResponsable = sinResponsable;
    }

    public int getTotal() {
        return total;
    }

    public Map<Estado, Long> getPorEstado() {
        return porEstado;
    }

    public Map<Prioridad, Long> getPorPrioridad() {
        return porPrioridad;
    }

    public Map<TipoSolicitud, Long> getPorTipo() {
        return porTipo;
    }

    public List<Solicitud> getUrgentes() {
        return urgentes;
    }

    public long getSinResponsable() {
        return sinResponsable;
    }
}
