package ar.edu.utn.solicitudes.dominio;

public class SolicitudNoEncontradaException extends ReglaNegocioException {

    public SolicitudNoEncontradaException(int id) {
        super("no existe la solicitud #" + id);
    }
}
