package com.healink.integrador.enums;

public enum EstadoAtencionMedica {
    EN_PROCESO("en_proceso"),
    COMPLETADA("completada"),
    CANCELADA("cancelada");

    private final String value;

    EstadoAtencionMedica(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EstadoAtencionMedica fromValue(String value) {
        for (EstadoAtencionMedica estado : EstadoAtencionMedica.values()) {
            if (estado.value.equalsIgnoreCase(value)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value);
    }
}
