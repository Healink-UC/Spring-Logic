package com.healink.integrador.domain.personal_medico;

import com.healink.integrador.core.dto.DTOBase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para Personal Médico")
public class PersonalMedicoDTO implements DTOBase {

    @Schema(description = "ID único del personal médico", readOnly = true)
    private Long id;

    @NotNull(message = "La especialidad es requerida")
    @Schema(description = "Especialidad médica del personal", example = "Cardiología")
    private String especialidad;

    @NotNull(message = "El ID de la entidad de salud es requerido")
    @Schema(description = "ID de la entidad de salud a la que pertenece")
    private Long entidadId;

    @NotNull(message = "El ID del usuario es requerido")
    @Schema(description = "ID del usuario asociado al personal médico")
    private Long usuarioId;

    @Schema(description = "Nombre de la entidad de salud", readOnly = true)
    private String entidadNombre;

    @Schema(description = "Nombre del usuario", readOnly = true)
    private String usuarioNombre;

    // Campos de auditoría heredados
    @Schema(description = "Fecha de creación", readOnly = true)
    private java.time.LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de actualización", readOnly = true)
    private java.time.LocalDateTime fechaActualizacion;

    @Schema(description = "ID del usuario que creó el registro", readOnly = true)
    private Long creadoPorId;

    @Schema(description = "ID del usuario que actualizó el registro", readOnly = true)
    private Long actualizadoPorId;
} 