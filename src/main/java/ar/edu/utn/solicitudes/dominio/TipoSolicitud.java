package ar.edu.utn.solicitudes.dominio;

public enum TipoSolicitud {
    CERTIFICADO,
    EQUIVALENCIA,
    CAMBIO_COMISION,
    RECLAMO;

    public static TipoSolicitud desde(String texto) {
        return Valores.convertir(TipoSolicitud.class, texto, "tipo de solicitud inválido");
    }
}
