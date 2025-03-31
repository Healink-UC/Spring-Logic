package com.healink.integrador.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SolicitudAcceso {
    @NotBlank
    private String tipoIdentificacion;

    @NotBlank
    private String identificacion;

    @NotBlank
    private String clave;
}
