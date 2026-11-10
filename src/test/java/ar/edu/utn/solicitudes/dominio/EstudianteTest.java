package ar.edu.utn.solicitudes.dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class EstudianteTest {

    @Test
    public void creaUnEstudianteValidoSinEspaciosSobrantes() {
        Estudiante estudiante = new Estudiante(" 100783 ", " Lucía Fernández ", " lucia@example.com ");

        assertEquals("100783", estudiante.getLegajo());
        assertEquals("Lucía Fernández", estudiante.getNombre());
        assertEquals("lucia@example.com", estudiante.getEmail());
    }

    @Test
    public void rechazaLegajoConFormatoIncorrecto() {
        verificarError("12", "Lucía", "lucia@example.com", "el legajo debe tener 5 o 6 dígitos");
    }

    @Test
    public void rechazaLegajoVacio() {
        verificarError("  ", "Lucía", "lucia@example.com", "el legajo es obligatorio");
    }

    @Test
    public void rechazaEmailSinArroba() {
        verificarError("100783", "Lucía", "correo-sin-arroba", "email inválido");
    }

    private void verificarError(String legajo, String nombre, String email, String mensajeEsperado) {
        try {
            new Estudiante(legajo, nombre, email);
            fail("Se esperaba ReglaNegocioException");
        } catch (ReglaNegocioException e) {
            assertEquals(mensajeEsperado, e.getMessage());
        }
    }
}
