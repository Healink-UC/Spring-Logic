package com.healink.integrador.enums;

public enum EstadoCampana {
    POSTULADA("POSTULADA"),
    ACTIVA("ACTIVA"),
    FINALIZADA("FINALIZADA"),
    CANCELADA("CANCELADA");

    private final String value;

    EstadoCampana(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EstadoCampana fromValue(String value) {
        for (EstadoCampana estado : EstadoCampana.values()) {
            if (estado.value.equalsIgnoreCase(value)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value);
    }
}
