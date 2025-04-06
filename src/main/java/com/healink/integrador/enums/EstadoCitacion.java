package com.healink.integrador.enums;

public enum EstadoCitacion {
    // AGENDADA|ATENDIDA|CANCELADA
    AGENDADA("AGENDADA"),
    ATENDIDA("ATENDIDA"),
    CANCELADA("CANCELADA");

    private final String value;

    EstadoCitacion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EstadoCitacion fromValue(String value) {
        for (EstadoCitacion estado : EstadoCitacion.values()) {
            if (estado.value.equalsIgnoreCase(value)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value);
    }
}
