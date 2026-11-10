package ar.edu.utn.solicitudes.dominio;

final class Valores {

    private Valores() {
    }

    static <E extends Enum<E>> E convertir(Class<E> tipo, String texto, String mensajeError) {
        if (texto == null) {
            throw new ReglaNegocioException(mensajeError);
        }
        try {
            return Enum.valueOf(tipo, normalizar(texto));
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException(mensajeError);
        }
    }

    static String normalizar(String texto) {
        return texto.trim().toUpperCase();
    }

    static String obligatorio(String texto, String mensajeError) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ReglaNegocioException(mensajeError);
        }
        return texto.trim();
    }
}
