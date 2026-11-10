package ar.edu.utn.solicitudes.dominio;

import java.util.EnumSet;
import java.util.Set;

public enum Estado {
    PENDIENTE,
    EN_PROCESO,
    RESUELTA,
    RECHAZADA;

    public boolean puedePasarA(Estado nuevo) {
        return transicionesPermitidas().contains(nuevo);
    }

    public boolean estaCerrada() {
        return this == RESUELTA || this == RECHAZADA;
    }

    private Set<Estado> transicionesPermitidas() {
        switch (this) {
            case PENDIENTE:
                return EnumSet.of(EN_PROCESO, RECHAZADA);
            case EN_PROCESO:
                return EnumSet.of(RESUELTA, RECHAZADA);
            default:
                return EnumSet.noneOf(Estado.class);
        }
    }

    public static Estado desde(String texto) {
        return Valores.convertir(Estado.class, texto, "estado inválido");
    }
}
