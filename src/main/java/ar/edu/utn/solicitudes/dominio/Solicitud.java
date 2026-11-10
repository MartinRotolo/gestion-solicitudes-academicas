package ar.edu.utn.solicitudes.dominio;

public class Solicitud {

    private static final int LARGO_MINIMO_DESCRIPCION = 10;
    private static final String SIN_RESPONSABLE = "(sin asignar)";

    private final int id;
    private final Estudiante estudiante;
    private final TipoSolicitud tipo;
    private final Prioridad prioridad;
    private final String descripcion;
    private Estado estado;
    private Responsable responsable;

    public Solicitud(int id, Estudiante estudiante, TipoSolicitud tipo, Prioridad prioridad, String descripcion,
            Estado estado, Responsable responsable) {
        this.id = id;
        this.estudiante = estudiante;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.descripcion = validarDescripcion(descripcion);
        this.estado = estado;
        this.responsable = responsable;
    }

    public static Solicitud nueva(int id, NuevaSolicitud datos) {
        return new Solicitud(id, datos.estudiante(), datos.tipo(), datos.prioridad(), datos.descripcion(),
                Estado.PENDIENTE, null);
    }

    private static String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().length() < LARGO_MINIMO_DESCRIPCION) {
            throw new ReglaNegocioException(
                    "la descripción debe tener al menos " + LARGO_MINIMO_DESCRIPCION + " caracteres");
        }
        return descripcion.trim();
    }

    public void cambiarEstado(Estado nuevo) {
        if (!estado.puedePasarA(nuevo)) {
            throw new ReglaNegocioException("no se puede pasar de " + estado + " a " + nuevo);
        }
        if (nuevo == Estado.EN_PROCESO && !tieneResponsable()) {
            throw new ReglaNegocioException("la solicitud debe tener un responsable asignado");
        }
        estado = nuevo;
    }

    public void asignarResponsable(Responsable nuevoResponsable) {
        if (estado.estaCerrada()) {
            throw new ReglaNegocioException("no se puede asignar responsable a una solicitud cerrada");
        }
        responsable = nuevoResponsable;
    }

    public boolean tieneResponsable() {
        return responsable != null;
    }

    public boolean esUrgente() {
        return prioridad == Prioridad.ALTA && !estado.estaCerrada();
    }

    public String resumen() {
        String textoResponsable = tieneResponsable() ? responsable.toString() : SIN_RESPONSABLE;
        return "#" + id + " [" + prioridad + "] " + tipo + " - " + estado + " - "
                + estudiante.getLegajo() + " " + estudiante.getNombre() + " - Resp: " + textoResponsable;
    }

    public int getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public TipoSolicitud getTipo() {
        return tipo;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Responsable getResponsable() {
        return responsable;
    }
}
