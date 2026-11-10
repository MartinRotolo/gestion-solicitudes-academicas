package ar.edu.utn.solicitudes.dominio;

/** Datos que ingresa el estudiante para registrar una solicitud. */
public class NuevaSolicitud {

    private final String legajo;
    private final String nombre;
    private final String email;
    private final String tipo;
    private final String prioridad;
    private final String descripcion;

    public NuevaSolicitud(String legajo, String nombre, String email, String tipo, String prioridad,
            String descripcion) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.email = email;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
    }

    public Estudiante estudiante() {
        return new Estudiante(legajo, nombre, email);
    }

    public TipoSolicitud tipo() {
        return TipoSolicitud.desde(tipo);
    }

    public Prioridad prioridad() {
        return Prioridad.desde(prioridad);
    }

    public String descripcion() {
        return descripcion;
    }
}
