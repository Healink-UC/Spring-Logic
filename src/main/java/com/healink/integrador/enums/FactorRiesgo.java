package com.healink.integrador.enums;

public enum FactorRiesgo {

    SOCIAL("social"),
    AMBIENTAL("ambiental"),
    RACIAL("racial");

    private final String value;

    FactorRiesgo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FactorRiesgo fromValue(String value) {
        for (FactorRiesgo factor : FactorRiesgo.values()) {
            if (factor.value.equalsIgnoreCase(value)) {
                return factor;
            }
        }
        throw new IllegalArgumentException("Factor de riesgo inválido: " + value);
    }
}
