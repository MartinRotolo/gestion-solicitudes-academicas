package ar.edu.utn.solicitudes.repositorio;

public class PersistenciaException extends RuntimeException {

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
