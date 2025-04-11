package com.healink.integrador.security;

import com.healink.integrador.enums.TipoIdentificacion;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SolicitudAcceso {

    @NotBlank
    @Enumerated(EnumType.STRING)
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank
    private String identificacion;

    @NotBlank
    private String clave;
}
