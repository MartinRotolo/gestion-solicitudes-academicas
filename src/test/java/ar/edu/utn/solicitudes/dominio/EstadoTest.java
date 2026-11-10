package ar.edu.utn.solicitudes.dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EstadoTest {

    @Test
    public void respetaLasTransicionesPermitidas() {
        assertTrue(Estado.PENDIENTE.puedePasarA(Estado.EN_PROCESO));
        assertTrue(Estado.PENDIENTE.puedePasarA(Estado.RECHAZADA));
        assertTrue(Estado.EN_PROCESO.puedePasarA(Estado.RESUELTA));
        assertFalse(Estado.PENDIENTE.puedePasarA(Estado.RESUELTA));
        assertFalse(Estado.RESUELTA.puedePasarA(Estado.PENDIENTE));
    }

    @Test
    public void convierteTextoSinImportarMayusculasNiEspacios() {
        assertEquals(Estado.EN_PROCESO, Estado.desde(" en_proceso "));
    }

    @Test(expected = ReglaNegocioException.class)
    public void rechazaUnEstadoInexistente() {
        Estado.desde("ARCHIVADA");
    }
}
