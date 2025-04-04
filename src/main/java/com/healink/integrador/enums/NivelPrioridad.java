package com.healink.integrador.enums;

public enum NivelPrioridad {
    ALTA("ALTA"),
    MEDIA("MEDIA"),
    BAJA("BAJA");

    private final String value;

    NivelPrioridad(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NivelPrioridad fromValue(String value) {
        for (NivelPrioridad nivel : NivelPrioridad.values()) {
            if (nivel.value.equalsIgnoreCase(value)) {
                return nivel;
            }
        }
        throw new IllegalArgumentException("Nivel de prioridad inválido: " + value);
    }
}
