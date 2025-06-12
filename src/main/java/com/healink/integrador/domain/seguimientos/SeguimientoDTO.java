package com.healink.integrador.domain.seguimientos;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeguimientoDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador de la citación médica es requerido")
    private Long citacion_id;

    @NotNull(message = "La fecha de programación es requerida")
    private LocalDate fecha_programada;
    
    private LocalDate fecha_realizada;
    
    @NotNull(message = "El tipo de seguimiento es requerido")
    private String tipo;

    @NotBlank(message = "El resultado es requerido")
    private String resultado;

    private String notas;

    @NotNull(message = "El estado es requerido")
    private String estado;

    @NotNull(message = "La prioridad es requerido")
    private String prioridad;


}