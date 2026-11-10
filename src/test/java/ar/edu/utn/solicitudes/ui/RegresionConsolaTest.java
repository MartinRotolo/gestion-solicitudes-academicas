package ar.edu.utn.solicitudes.ui;

import static org.junit.Assert.assertEquals;

import ar.edu.utn.solicitudes.repositorio.CsvSolicitudRepository;
import ar.edu.utn.solicitudes.servicio.ReporteService;
import ar.edu.utn.solicitudes.servicio.SolicitudService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import org.junit.Test;

/**
 * Prueba de regresión: ejecuta el escenario de pruebas/escenario_regresion.txt y compara la salida
 * de consola y el CSV generado con los obtenidos con la versión inicial (tag estado-inicial).
 */
public class RegresionConsolaTest {

    private static final Path ESCENARIO = Paths.get("pruebas", "escenario_regresion.txt");
    private static final Path SALIDA_INICIAL = Paths.get("pruebas", "salida_estado_inicial.txt");
    private static final Path DATOS_INICIALES = Paths.get("pruebas", "datos_estado_inicial.csv");

    @Test
    public void laVersionRefactorizadaProduceLaMismaSalidaQueLaInicial() throws Exception {
        Path archivo = Files.createTempFile("solicitudes", ".csv");
        Files.delete(archivo);
        CsvSolicitudRepository repositorio = new CsvSolicitudRepository(archivo);
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Scanner entrada = new Scanner(new ByteArrayInputStream(Files.readAllBytes(ESCENARIO)), "UTF-8");

        new ConsolaApp(new SolicitudService(repositorio), new ReporteService(repositorio), entrada,
                new PrintStream(salida, true, "UTF-8")).ejecutar();

        assertEquals(normalizar(new String(Files.readAllBytes(SALIDA_INICIAL), StandardCharsets.UTF_8)),
                normalizar(salida.toString("UTF-8")));
        assertEquals(Files.readAllLines(DATOS_INICIALES, StandardCharsets.UTF_8),
                Files.readAllLines(archivo, StandardCharsets.UTF_8));
    }

    private static String normalizar(String texto) {
        return texto.replace("\r\n", "\n").trim();
    }
}
