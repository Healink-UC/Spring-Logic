package com.healink.integrador.domain.prescripciones;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescripcionDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador del diagnostico es requerido")
    private Long diagnostico_id;

    @NotNull(message = "El tipo de prescripción es requerido")
    private String tipo;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotBlank(message = "Es requerido indicar la dosis")
    private String dosis;
    
    @NotBlank(message = "Es requerido indicar la frecuencia")
    private String frecuencia;

    @NotBlank(message = "Es requerido indicar la dosis")
    private String duracion;

    private String indicaciones_especiales;

}