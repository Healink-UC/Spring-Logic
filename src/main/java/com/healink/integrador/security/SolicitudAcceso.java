package com.healink.integrador.security;

import com.healink.integrador.enums.TipoIdentificacion;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SolicitudAcceso {
    @NotBlank
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank
    private String identificacion;

    @NotBlank
    private String clave;
}
