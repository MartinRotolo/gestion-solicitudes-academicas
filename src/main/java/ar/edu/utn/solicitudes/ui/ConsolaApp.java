package ar.edu.utn.solicitudes.ui;

import ar.edu.utn.solicitudes.dominio.Estado;
import ar.edu.utn.solicitudes.dominio.FiltroSolicitudes;
import ar.edu.utn.solicitudes.dominio.NuevaSolicitud;
import ar.edu.utn.solicitudes.dominio.ReglaNegocioException;
import ar.edu.utn.solicitudes.dominio.Solicitud;
import ar.edu.utn.solicitudes.servicio.Reporte;
import ar.edu.utn.solicitudes.servicio.ReporteService;
import ar.edu.utn.solicitudes.servicio.SolicitudService;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/** Interfaz de consola: lee los datos, llama a los servicios y muestra los resultados. */
public class ConsolaApp {

    private final SolicitudService solicitudes;
    private final ReporteService reportes;
    private final Scanner entrada;
    private final PrintStream salida;

    public ConsolaApp(SolicitudService solicitudes, ReporteService reportes, Scanner entrada, PrintStream salida) {
        this.solicitudes = solicitudes;
        this.reportes = reportes;
        this.entrada = entrada;
        this.salida = salida;
    }

    public void ejecutar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            if (!entrada.hasNextLine()) {
                break;
            }
            salir = procesarOpcion(entrada.nextLine().trim());
        }
    }

    private void mostrarMenu() {
        salida.println();
        salida.println("=== GESTIÓN DE SOLICITUDES ACADÉMICAS ===");
        salida.println("1. Registrar solicitud");
        salida.println("2. Cambiar estado");
        salida.println("3. Asignar responsable");
        salida.println("4. Buscar / filtrar");
        salida.println("5. Listar todas");
        salida.println("6. Reporte");
        salida.println("0. Salir");
        salida.print("Opción: ");
    }

    private boolean procesarOpcion(String opcion) {
        try {
            switch (opcion) {
                case "1": registrarSolicitud(); break;
                case "2": cambiarEstado(); break;
                case "3": asignarResponsable(); break;
                case "4": buscarSolicitudes(); break;
                case "5": listarSolicitudes(); break;
                case "6": mostrarReporte(); break;
                case "0":
                    salida.println("Hasta luego");
                    return true;
                default:
                    salida.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            mostrarError("el número de solicitud debe ser numérico");
        } catch (ReglaNegocioException e) {
            mostrarError(e.getMessage());
        }
        return false;
    }

    private void registrarSolicitud() {
        NuevaSolicitud datos = new NuevaSolicitud(
                preguntar("Legajo: "),
                preguntar("Nombre: "),
                preguntar("Email: "),
                preguntar("Tipo (CERTIFICADO/EQUIVALENCIA/CAMBIO_COMISION/RECLAMO): "),
                preguntar("Prioridad (ALTA/MEDIA/BAJA): "),
                preguntar("Descripción: "));
        Solicitud solicitud = solicitudes.registrar(datos);
        salida.println("Solicitud #" + solicitud.getId() + " registrada");
    }

    private void cambiarEstado() {
        int id = preguntarNumeroSolicitud();
        String nuevoEstado = preguntar("Nuevo estado (PENDIENTE/EN_PROCESO/RESUELTA/RECHAZADA): ");
        Estado anterior = solicitudes.cambiarEstado(id, nuevoEstado);
        salida.println("Solicitud #" + id + ": " + anterior + " -> " + Estado.desde(nuevoEstado));
    }

    private void asignarResponsable() {
        int id = preguntarNumeroSolicitud();
        String nombre = preguntar("Responsable: ");
        String area = preguntar("Área: ");
        Solicitud solicitud = solicitudes.asignarResponsable(id, nombre, area);
        salida.println("Solicitud #" + id + " asignada a " + solicitud.getResponsable().getNombre()
                + " (" + solicitud.getResponsable().getArea() + ")");
    }

    private void buscarSolicitudes() {
        FiltroSolicitudes filtro = new FiltroSolicitudes(
                preguntar("Tipo (vacío = todos): "),
                preguntar("Estado (vacío = todos): "),
                preguntar("Prioridad (vacío = todas): "));
        mostrarLista("Resultados: ", solicitudes.buscar(filtro));
    }

    private void listarSolicitudes() {
        mostrarLista("Total de solicitudes: ", solicitudes.listar());
    }

    private void mostrarLista(String titulo, List<Solicitud> lista) {
        salida.println(titulo + lista.size());
        lista.forEach(solicitud -> salida.println(solicitud.resumen()));
    }

    private void mostrarReporte() {
        Reporte reporte = reportes.generar();
        salida.println("===== REPORTE DE SOLICITUDES =====");
        salida.println("Total: " + reporte.getTotal());
        mostrarConteo("Por estado:", reporte.getPorEstado());
        mostrarConteo("Por prioridad:", reporte.getPorPrioridad());
        mostrarConteo("Por tipo:", reporte.getPorTipo());
        salida.println("Urgentes (prioridad ALTA sin cerrar): " + reporte.getUrgentes().size());
        for (Solicitud solicitud : reporte.getUrgentes()) {
            salida.println("  #" + solicitud.getId() + " " + solicitud.getTipo() + " - "
                    + solicitud.getEstudiante().getNombre() + " - " + solicitud.getEstado());
        }
        salida.println("Sin responsable asignado: " + reporte.getSinResponsable());
        salida.println("==================================");
    }

    private void mostrarConteo(String titulo, Map<? extends Enum<?>, Long> conteo) {
        salida.println(titulo);
        conteo.forEach((valor, cantidad) -> salida.println("  " + valor + ": " + cantidad));
    }

    private int preguntarNumeroSolicitud() {
        return Integer.parseInt(preguntar("Número de solicitud: ").trim());
    }

    private String preguntar(String texto) {
        salida.print(texto);
        return entrada.nextLine();
    }

    private void mostrarError(String mensaje) {
        salida.println("Error: " + mensaje);
    }
}
