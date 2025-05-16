package com.healink.integrador.domain.inscripciones_campana;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InscripcionCampanaDTO implements DTOBase {

    @Schema(readOnly = true)
    private Long id;

    @NotNull(message = "El ID del paciente es requerido")
    private Long pacienteId;

    @NotNull(message = "El ID de la campaña es requerido")
    private Long campanaId;

    @Schema(readOnly = true)
    private LocalDateTime fechaInscripcion;

    @Schema(readOnly = true)
    private EstadoInscripcion estado;

    private String motivoRetiro;
}