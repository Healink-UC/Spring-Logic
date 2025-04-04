package com.healink.integrador.enums;

public enum TipoPrescripcion {
    MEDICAMENTO("MEDICAMENTO"),
    ESTILO_VIDA("ESTILO_VIDA"),
    ACTIVIDAD_FISICA("ACTIVIDAD_FISICA"),
    DIETA("DIETA");

    private final String value;

    TipoPrescripcion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TipoPrescripcion fromValue(String value) {
        for (TipoPrescripcion tipo : TipoPrescripcion.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de prescripción inválido: " + value);
    }

}
