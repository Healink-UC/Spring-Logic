package com.healink.integrador.domain.inscripciones_campana;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InscripcionCampanaDTO implements DTOBase {

    @Schema(description = "ID único de la inscripción", readOnly = true)
    private Long id;

    @NotNull(message = "El ID del usuario es requerido")
    @Schema(description = "ID del usuario que se inscribe", example = "1")
    private Long usuarioId;

    @NotNull(message = "El ID de la campaña es requerido")
    @Schema(description = "ID de la campaña a la que se inscribe", example = "1")
    private Long campanaId;

    @Schema(description = "Fecha y hora de inscripción", readOnly = true)
    private LocalDateTime fechaInscripcion;

    @Schema(description = "Estado actual de la inscripción", readOnly = true)
    private EstadoInscripcion estado;

    @Schema(description = "Motivo del retiro (solo aplicable cuando estado es RETIRADO)", readOnly = true)
    private String motivoRetiro;
}