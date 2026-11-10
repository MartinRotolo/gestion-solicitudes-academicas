package ar.edu.utn.solicitudes;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String archivo = "solicitudes.csv";
        if (args.length > 0) {
            archivo = args[0];
        }
        GestorSolicitudes g = new GestorSolicitudes(archivo);
        g.cargar();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("=== GESTIÓN DE SOLICITUDES ACADÉMICAS ===");
            System.out.println("1. Registrar solicitud");
            System.out.println("2. Cambiar estado");
            System.out.println("3. Asignar responsable");
            System.out.println("4. Buscar / filtrar");
            System.out.println("5. Listar todas");
            System.out.println("6. Reporte");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            if (!sc.hasNextLine()) {
                break;
            }
            String op = sc.nextLine().trim();
            try {
                if (op.equals("1")) {
                    System.out.print("Legajo: ");
                    String leg = sc.nextLine();
                    System.out.print("Nombre: ");
                    String nom = sc.nextLine();
                    System.out.print("Email: ");
                    String mail = sc.nextLine();
                    System.out.print("Tipo (CERTIFICADO/EQUIVALENCIA/CAMBIO_COMISION/RECLAMO): ");
                    String t = sc.nextLine();
                    System.out.print("Prioridad (ALTA/MEDIA/BAJA): ");
                    String p = sc.nextLine();
                    System.out.print("Descripción: ");
                    String desc = sc.nextLine();
                    g.registrar(leg, nom, mail, t, p, desc);
                } else if (op.equals("2")) {
                    System.out.print("Número de solicitud: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Nuevo estado (PENDIENTE/EN_PROCESO/RESUELTA/RECHAZADA): ");
                    String e = sc.nextLine();
                    g.cambiarEstado(id, e);
                } else if (op.equals("3")) {
                    System.out.print("Número de solicitud: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Responsable: ");
                    String n = sc.nextLine();
                    System.out.print("Área: ");
                    String a = sc.nextLine();
                    g.asignar(id, n, a);
                } else if (op.equals("4")) {
                    System.out.print("Tipo (vacío = todos): ");
                    String t = sc.nextLine();
                    System.out.print("Estado (vacío = todos): ");
                    String e = sc.nextLine();
                    System.out.print("Prioridad (vacío = todas): ");
                    String p = sc.nextLine();
                    g.buscar(t, e, p);
                } else if (op.equals("5")) {
                    g.listar();
                } else if (op.equals("6")) {
                    g.reporte();
                } else if (op.equals("0")) {
                    salir = true;
                    System.out.println("Hasta luego");
                } else {
                    System.out.println("Opción inválida");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error: el número de solicitud debe ser numérico");
            }
        }
    }
}
