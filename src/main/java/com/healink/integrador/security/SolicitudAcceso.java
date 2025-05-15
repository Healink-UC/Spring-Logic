package com.healink.integrador.security;

import com.healink.integrador.domain.usuario.TipoIdentificacion;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAcceso {

    @NotBlank
    @Enumerated(EnumType.STRING)
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank
    private String identificacion;

    @NotBlank
    private String clave;
}
