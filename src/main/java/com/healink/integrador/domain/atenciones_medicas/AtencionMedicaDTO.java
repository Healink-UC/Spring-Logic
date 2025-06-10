package com.healink.integrador.domain.atenciones_medicas;

import java.sql.Timestamp;

import com.healink.integrador.core.dto.DTOBase;
import com.healink.integrador.domain.atenciones_medicas.validators.ValidAtencionMedicaFechas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@ValidAtencionMedicaFechas
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
    @Min(value = 15, message = "La duración real debe ser mayor o igual a 15 minutos")
    @Max(value = 60, message = "La duración real debe ser menor o igual a 60 minutos")
    private int duracionReal;

    @NotNull(message = "El estado es requerido")
    private String estado;

}