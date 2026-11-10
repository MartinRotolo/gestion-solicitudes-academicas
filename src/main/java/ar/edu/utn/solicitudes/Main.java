package ar.edu.utn.solicitudes;

import ar.edu.utn.solicitudes.repositorio.CsvSolicitudRepository;
import ar.edu.utn.solicitudes.repositorio.SolicitudRepository;
import ar.edu.utn.solicitudes.servicio.ReporteService;
import ar.edu.utn.solicitudes.servicio.SolicitudService;
import ar.edu.utn.solicitudes.ui.ConsolaApp;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    private static final String ARCHIVO_POR_DEFECTO = "solicitudes.csv";

    public static void main(String[] args) {
        Path archivo = Path.of(args.length > 0 ? args[0] : ARCHIVO_POR_DEFECTO);
        SolicitudRepository repositorio = new CsvSolicitudRepository(archivo);
        ConsolaApp app = new ConsolaApp(new SolicitudService(repositorio), new ReporteService(repositorio),
                new Scanner(System.in), System.out);
        app.ejecutar();
    }
}
