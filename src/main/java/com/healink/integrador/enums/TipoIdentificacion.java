package com.healink.integrador.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoIdentificacion {
    CC("cc"),
    TI("ti"),
    NIT("nit"),
    RCN("rcn");

    private final String value;

    TipoIdentificacion(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TipoIdentificacion fromValue(String value) {
        for (TipoIdentificacion tipo : TipoIdentificacion.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de identificación inválido: " + value);
    }
}
