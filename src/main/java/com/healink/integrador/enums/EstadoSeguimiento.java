package com.healink.integrador.enums;

public enum EstadoSeguimiento {
    PENDIENTE("pendiente"),
    REALIZADO("realizado"),
    CANCELADO("cancelado");

    private final String value;

    EstadoSeguimiento(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EstadoSeguimiento fromValue(String value) {
        for (EstadoSeguimiento estado : EstadoSeguimiento.values()) {
            if (estado.value.equalsIgnoreCase(value)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value);
    }
}
