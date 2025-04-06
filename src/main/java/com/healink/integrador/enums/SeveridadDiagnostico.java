package com.healink.integrador.enums;

public enum SeveridadDiagnostico {
    LEVE("LEVE"),
    MODERADA("MODERADA"),
    GRAVE("GRAVE");

    private final String value;

    SeveridadDiagnostico(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SeveridadDiagnostico fromValue(String value) {
        for (SeveridadDiagnostico severidad : SeveridadDiagnostico.values()) {
            if (severidad.value.equalsIgnoreCase(value)) {
                return severidad;
            }
        }
        throw new IllegalArgumentException("Severidad inválida: " + value);
    }
}
