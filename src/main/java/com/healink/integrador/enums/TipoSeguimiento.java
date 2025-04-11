package com.healink.integrador.enums;

public enum TipoSeguimiento {
    LLAMADA("LLAMADA"),
    SMS("SMS"),
    PRESENCIAL("PRESENCIAL");

    private final String value;

    TipoSeguimiento(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TipoSeguimiento fromValue(String value) {
        for (TipoSeguimiento tipo : TipoSeguimiento.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de seguimiento inválido: " + value);
    }

}
