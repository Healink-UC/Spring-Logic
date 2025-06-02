package com.healink.integrador.domain.atenciones_medicas;

import java.sql.Timestamp;

import com.healink.integrador.core.dto.DTOBase;
import com.healink.integrador.core.validator.FechasValidator;
import com.healink.integrador.core.validator.TimeValidator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtencionMedicaDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador de la citación es requerido")
    private Long citacionId;

    @NotNull(message = "La fecha y hora de inicio es requerida")
    @TimeValidator(compareWith = "fechaHoraFin", message = "La fecha de inicio debe ser anterior a la fecha de finalizacion de la atencion medica.")
    private Timestamp fechaHoraInicio;

    @NotNull(message = "La fecha y hora de fin es requerida")
    @TimeValidator(compareWith = "fechaHoraInicio", message = "La fecha de fin debe ser posterior a la fecha de inicio de la atencion medica.")
    private Timestamp fechaHoraFin;

    @NotNull(message = "La duración real es requerida")
    @Min(value = 5, message = "La duración real debe ser mayor o igual a 5 minutos")
    @Max(value = 60, message = "La duración real debe ser menor o igual a 60 minutos")
    private int duracionReal;

    @NotNull(message = "El estado es requerido")
    private String estado;

}