package ar.edu.utn.solicitudes.dominio;

public class Responsable {

    private final String nombre;
    private final String area;

    public Responsable(String nombre, String area) {
        this.nombre = Valores.obligatorio(nombre, "el nombre del responsable es obligatorio");
        this.area = Valores.normalizar(Valores.obligatorio(area, "el área es obligatoria"));
    }

    public String getNombre() {
        return nombre;
    }

    public String getArea() {
        return area;
    }

    @Override
    public String toString() {
        return nombre + " - " + area;
    }
}
