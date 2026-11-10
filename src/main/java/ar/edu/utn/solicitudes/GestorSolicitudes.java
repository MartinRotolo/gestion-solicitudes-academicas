package ar.edu.utn.solicitudes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Clase principal del sistema: maneja todo
public class GestorSolicitudes {

    private List<Solicitud> l = new ArrayList<>();
    private int c = 1;
    private String archivo;

    public GestorSolicitudes(String archivo) {
        this.archivo = archivo;
    }

    // registra una solicitud nueva
    public void registrar(String leg, String nom, String mail, String t, String p, String desc) {
        if (leg == null || leg.trim().isEmpty()) {
            System.out.println("Error: el legajo es obligatorio");
            return;
        }
        if (!leg.trim().matches("\\d{5,6}")) {
            System.out.println("Error: el legajo debe tener 5 o 6 dígitos");
            return;
        }
        if (nom == null || nom.trim().isEmpty()) {
            System.out.println("Error: el nombre es obligatorio");
            return;
        }
        if (mail == null || !mail.contains("@") || !mail.contains(".")) {
            System.out.println("Error: email inválido");
            return;
        }
        t = t.trim().toUpperCase();
        if (!t.equals("CERTIFICADO") && !t.equals("EQUIVALENCIA") && !t.equals("CAMBIO_COMISION") && !t.equals("RECLAMO")) {
            System.out.println("Error: tipo de solicitud inválido");
            return;
        }
        p = p.trim().toUpperCase();
        if (!p.equals("ALTA") && !p.equals("MEDIA") && !p.equals("BAJA")) {
            System.out.println("Error: prioridad inválida");
            return;
        }
        if (desc == null || desc.trim().length() < 10) {
            System.out.println("Error: la descripción debe tener al menos 10 caracteres");
            return;
        }
        Solicitud s = new Solicitud();
        s.id = c;
        c++;
        Estudiante e = new Estudiante();
        e.legajo = leg.trim();
        e.nombre = nom.trim();
        e.email = mail.trim();
        s.estudiante = e;
        s.tipo = t;
        s.prioridad = p;
        s.estado = "PENDIENTE";
        s.descripcion = desc.trim();
        l.add(s);
        guardar();
        System.out.println("Solicitud #" + s.id + " registrada");
    }

    // cambia el estado
    public void cambiarEstado(int id, String nuevo) {
        Solicitud s = null;
        for (int x = 0; x < l.size(); x++) {
            if (l.get(x).id == id) {
                s = l.get(x);
            }
        }
        if (s == null) {
            System.out.println("Error: no existe la solicitud #" + id);
            return;
        }
        nuevo = nuevo.trim().toUpperCase();
        if (!nuevo.equals("PENDIENTE") && !nuevo.equals("EN_PROCESO") && !nuevo.equals("RESUELTA") && !nuevo.equals("RECHAZADA")) {
            System.out.println("Error: estado inválido");
            return;
        }
        boolean ok = false;
        if (s.estado.equals("PENDIENTE")) {
            if (nuevo.equals("EN_PROCESO") || nuevo.equals("RECHAZADA")) {
                ok = true;
            }
        } else if (s.estado.equals("EN_PROCESO")) {
            if (nuevo.equals("RESUELTA") || nuevo.equals("RECHAZADA")) {
                ok = true;
            }
        } else if (s.estado.equals("RESUELTA")) {
            ok = false;
        } else if (s.estado.equals("RECHAZADA")) {
            ok = false;
        }
        if (!ok) {
            System.out.println("Error: no se puede pasar de " + s.estado + " a " + nuevo);
            return;
        }
        if (nuevo.equals("EN_PROCESO") && s.responsable == null) {
            System.out.println("Error: la solicitud debe tener un responsable asignado");
            return;
        }
        String anterior = s.estado;
        s.estado = nuevo;
        guardar();
        System.out.println("Solicitud #" + id + ": " + anterior + " -> " + nuevo);
    }

    // asigna responsable
    public void asignar(int id, String nombre, String area) {
        Solicitud s = null;
        for (int x = 0; x < l.size(); x++) {
            if (l.get(x).id == id) {
                s = l.get(x);
            }
        }
        if (s == null) {
            System.out.println("Error: no existe la solicitud #" + id);
            return;
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: el nombre del responsable es obligatorio");
            return;
        }
        if (area == null || area.trim().isEmpty()) {
            System.out.println("Error: el área es obligatoria");
            return;
        }
        if (s.estado.equals("RESUELTA") || s.estado.equals("RECHAZADA")) {
            System.out.println("Error: no se puede asignar responsable a una solicitud cerrada");
            return;
        }
        Responsable r = new Responsable();
        r.nombre = nombre.trim();
        r.area = area.trim().toUpperCase();
        s.responsable = r;
        guardar();
        System.out.println("Solicitud #" + id + " asignada a " + r.nombre + " (" + r.area + ")");
    }

    // busca por filtros, vacio = todos
    public void buscar(String t, String e, String p) {
        t = t.trim().toUpperCase();
        e = e.trim().toUpperCase();
        p = p.trim().toUpperCase();
        List<Solicitud> res = new ArrayList<>();
        for (int x = 0; x < l.size(); x++) {
            Solicitud s = l.get(x);
            if (!t.isEmpty() && !s.tipo.equals(t)) {
                continue;
            }
            if (!e.isEmpty() && !s.estado.equals(e)) {
                continue;
            }
            if (!p.isEmpty() && !s.prioridad.equals(p)) {
                continue;
            }
            res.add(s);
        }
        System.out.println("Resultados: " + res.size());
        for (int x = 0; x < res.size(); x++) {
            Solicitud s = res.get(x);
            String resp = "(sin asignar)";
            if (s.responsable != null) {
                resp = s.responsable.nombre + " - " + s.responsable.area;
            }
            System.out.println("#" + s.id + " [" + s.prioridad + "] " + s.tipo + " - " + s.estado + " - "
                    + s.estudiante.legajo + " " + s.estudiante.nombre + " - Resp: " + resp);
        }
    }

    // lista todas
    public void listar() {
        System.out.println("Total de solicitudes: " + l.size());
        for (int x = 0; x < l.size(); x++) {
            Solicitud s = l.get(x);
            String resp = "(sin asignar)";
            if (s.responsable != null) {
                resp = s.responsable.nombre + " - " + s.responsable.area;
            }
            System.out.println("#" + s.id + " [" + s.prioridad + "] " + s.tipo + " - " + s.estado + " - "
                    + s.estudiante.legajo + " " + s.estudiante.nombre + " - Resp: " + resp);
        }
    }

    // reporte general
    public void reporte() {
        int pen = 0, proc = 0, res = 0, rec = 0;
        int a = 0, m = 0, b = 0;
        int cert = 0, eq = 0, cc = 0, recl = 0;
        int sinResp = 0;
        List<Solicitud> urg = new ArrayList<>();
        for (int x = 0; x < l.size(); x++) {
            Solicitud s = l.get(x);
            if (s.estado.equals("PENDIENTE")) {
                pen++;
            } else if (s.estado.equals("EN_PROCESO")) {
                proc++;
            } else if (s.estado.equals("RESUELTA")) {
                res++;
            } else if (s.estado.equals("RECHAZADA")) {
                rec++;
            }
            if (s.prioridad.equals("ALTA")) {
                a++;
            } else if (s.prioridad.equals("MEDIA")) {
                m++;
            } else {
                b++;
            }
            if (s.tipo.equals("CERTIFICADO")) {
                cert++;
            } else if (s.tipo.equals("EQUIVALENCIA")) {
                eq++;
            } else if (s.tipo.equals("CAMBIO_COMISION")) {
                cc++;
            } else {
                recl++;
            }
            if (s.responsable == null) {
                sinResp++;
            }
            if (s.prioridad.equals("ALTA") && !s.estado.equals("RESUELTA") && !s.estado.equals("RECHAZADA")) {
                urg.add(s);
            }
        }
        System.out.println("===== REPORTE DE SOLICITUDES =====");
        System.out.println("Total: " + l.size());
        System.out.println("Por estado:");
        System.out.println("  PENDIENTE: " + pen);
        System.out.println("  EN_PROCESO: " + proc);
        System.out.println("  RESUELTA: " + res);
        System.out.println("  RECHAZADA: " + rec);
        System.out.println("Por prioridad:");
        System.out.println("  ALTA: " + a);
        System.out.println("  MEDIA: " + m);
        System.out.println("  BAJA: " + b);
        System.out.println("Por tipo:");
        System.out.println("  CERTIFICADO: " + cert);
        System.out.println("  EQUIVALENCIA: " + eq);
        System.out.println("  CAMBIO_COMISION: " + cc);
        System.out.println("  RECLAMO: " + recl);
        System.out.println("Urgentes (prioridad ALTA sin cerrar): " + urg.size());
        for (int x = 0; x < urg.size(); x++) {
            Solicitud s = urg.get(x);
            System.out.println("  #" + s.id + " " + s.tipo + " - " + s.estudiante.nombre + " - " + s.estado);
        }
        System.out.println("Sin responsable asignado: " + sinResp);
        System.out.println("==================================");
    }

    // guarda en el archivo
    private void guardar() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(archivo));
            for (int x = 0; x < l.size(); x++) {
                Solicitud s = l.get(x);
                String rn = "";
                String ra = "";
                if (s.responsable != null) {
                    rn = s.responsable.nombre;
                    ra = s.responsable.area;
                }
                pw.println(s.id + ";" + s.estudiante.legajo + ";" + s.estudiante.nombre + ";" + s.estudiante.email + ";"
                        + s.tipo + ";" + s.prioridad + ";" + s.estado + ";" + s.descripcion + ";" + rn + ";" + ra);
            }
            pw.close();
        } catch (Exception ex) {
        }
    }

    // carga desde el archivo
    public void cargar() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";", -1);
                Solicitud s = new Solicitud();
                s.id = Integer.parseInt(d[0]);
                Estudiante e = new Estudiante();
                e.legajo = d[1];
                e.nombre = d[2];
                e.email = d[3];
                s.estudiante = e;
                s.tipo = d[4];
                s.prioridad = d[5];
                s.estado = d[6];
                s.descripcion = d[7];
                if (!d[8].isEmpty()) {
                    Responsable r = new Responsable();
                    r.nombre = d[8];
                    r.area = d[9];
                    s.responsable = r;
                }
                l.add(s);
                if (s.id >= c) {
                    c = s.id + 1;
                }
            }
            br.close();
        } catch (Exception ex) {
        }
    }
}
