package com.healink.integrador.domain.factor_riesgo;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FactorRiesgoDTO implements DTOBase {

    private Long id;

    @NotBlank(message = "El nombre de factor de riesgo es requerido")
    private String nombre;

    @NotBlank(message = "La descripcion del factor de riesgo es requerida")
    private String descripcion;

    @NotNull(message = "El tipo de factor de riesgo es requerido")
    private TipoFactorRiesgo tipo;

}
