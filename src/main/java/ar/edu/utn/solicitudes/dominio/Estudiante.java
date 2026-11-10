package ar.edu.utn.solicitudes.dominio;

public class Estudiante {

    private static final String FORMATO_LEGAJO = "\\d{5,6}";

    private final String legajo;
    private final String nombre;
    private final String email;

    public Estudiante(String legajo, String nombre, String email) {
        this.legajo = validarLegajo(legajo);
        this.nombre = Valores.obligatorio(nombre, "el nombre es obligatorio");
        this.email = validarEmail(email);
    }

    private static String validarLegajo(String legajo) {
        String valor = Valores.obligatorio(legajo, "el legajo es obligatorio");
        if (!valor.matches(FORMATO_LEGAJO)) {
            throw new ReglaNegocioException("el legajo debe tener 5 o 6 dígitos");
        }
        return valor;
    }

    private static String validarEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ReglaNegocioException("email inválido");
        }
        return email.trim();
    }

    public String getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}
