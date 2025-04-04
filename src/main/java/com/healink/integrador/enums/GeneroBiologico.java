package com.healink.integrador.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GeneroBiologico {
    MASCULINO("M"),
    FEMENINO("F");

    private final String value;

    GeneroBiologico(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static GeneroBiologico fromValue(String value) {
        for (GeneroBiologico estado : GeneroBiologico.values()) {
            if (estado.value.equalsIgnoreCase(value)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value);
    }
}
