package com.healink.integrador.domain.atenciones_medicas;

import java.sql.Timestamp;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El estado es requerido")
    private String estado;

}