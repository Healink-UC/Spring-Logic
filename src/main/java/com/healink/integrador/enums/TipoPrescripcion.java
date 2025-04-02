package com.healink.integrador.enums;

public enum TipoPrescripcion {
    MEDICAMENTO("medicamento"),
    ESTILO_VIDA("estilo_vida"),
    ACTIVIDAD_FISICA("actividad_fisica"),
    DIETA("dieta");

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
