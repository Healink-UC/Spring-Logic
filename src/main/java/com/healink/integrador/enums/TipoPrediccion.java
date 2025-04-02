package com.healink.integrador.enums;

public enum TipoPrediccion {
    RIESGO_CV("riesgo_cv"),
    ASISTENCIA("asistencia"),
    HOSPITALIZACION("hospitalizacion"),
    REHOSPITALIZACION("rehospitalizacion");

    private final String value;

    TipoPrediccion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TipoPrediccion fromValue(String value) {
        for (TipoPrediccion tipo : TipoPrediccion.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de predicción inválido: " + value);
    }

}
