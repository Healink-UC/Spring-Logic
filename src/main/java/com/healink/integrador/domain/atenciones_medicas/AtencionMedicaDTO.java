package com.healink.integrador.domain.atenciones_medicas;

import java.sql.Timestamp;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AtencionMedicaDTO implements DTOBase {

    @Schema(readOnly = true, description = "ID de la atención médica")
    private Long id;

    @Schema(description = "ID de la citación médica asociada")
    private Long citacionId;

    @Schema(description = "Fecha y hora de inicio de la atención")
    private Timestamp fechaHoraInicio;

    @Schema(description = "Fecha y hora de fin de la atención")
    private Timestamp fechaHoraFin;

    @Schema(description = "Duración real de la atención en minutos")
    @Min(value = 5, message = "La duración real debe ser mayor o igual a 5 minutos")
    @Max(value = 60, message = "La duración real debe ser menor o igual a 60 minutos")
    private Integer duracionReal;

    @Schema(description = "Estado de la atención médica")
    private String estado;

}