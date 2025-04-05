package com.healink.integrador.domain.atenciones_medicas;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AtencionMedicaDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador de la citación es requerido")
    private Long citacionId;

    @NotNull(message = "La fecha y hora de inicio es requerida")
    private Timestamp fechaHoraInicio;

    @NotNull(message = "La fecha y hora de fin es requerida")
    private Timestamp fechaHoraFin;

    @NotNull(message = "La duración real es requerida")
    private int duracionReal;

    @NotBlank(message = "Las notas médicas son requeridas")
    private String notasMedicas;

    private String notasEstructuradas;

    @NotBlank(message = "El estado es requerido")
    @Pattern(regexp = "^(EN_PROCESO|COMPLETADA|CANCELADA)$", message = "Estado de atención médica inválido. Debe ser 'EN_PROCESO', 'PROCESADA' o 'CANCELADA'.")
    private String estado;

}