package com.healink.integrador.enums;

public enum EstadoCampana {
    POSTULADA("postulada"),
    ACTIVA("activa"),
    FINALIZADA("finalizada"),
    CANCELADA("cancelada");

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
