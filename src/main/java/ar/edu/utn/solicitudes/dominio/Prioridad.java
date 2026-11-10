package ar.edu.utn.solicitudes.dominio;

public enum Prioridad {
    ALTA,
    MEDIA,
    BAJA;

    public static Prioridad desde(String texto) {
        return Valores.convertir(Prioridad.class, texto, "prioridad inválida");
    }
}
