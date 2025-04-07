package com.healink.integrador.enums;

public enum TipoRecomendaciones {
    MEDICAMENTO("MEDICAMENTO"),
    ESTILO_VIDA("ESTILO_VIDA"),
    PREVENCION("PREVENCION");

    private final String value;

    TipoRecomendaciones(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TipoRecomendaciones fromValue(String value) {
        for (TipoRecomendaciones tipo : TipoRecomendaciones.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de recomendación inválido: " + value);
    }
}
