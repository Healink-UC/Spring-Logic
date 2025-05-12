package com.healink.integrador.domain.predicciones;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PrediccionDTO implements DTOBase {

    @Schema(readOnly = true, description = "Identificador único de la predicción")
    private Long id;

    @NotNull(message = "El id del paciente es requerido")
    private Long pacienteId;

    @NotNull(message = "El id de la campaña es requerido")
    private Long campanaId;

    @NotNull(message = "El valor de predicción es requerido")
    @Min(value = 0, message = "El valor de predicción debe ser entre 0 y 100")
    @Max(value = 100, message = "El valor de predicción debe ser entre 0 y 100")
    private float valorPrediccion;

    @NotNull(message = "El nivel de confianza es requerido")
    @Min(value = 0, message = "El nivel de confianza debe ser entre 0 y 100")
    @Max(value = 100, message = "El nivel de confianza debe ser entre 0 y 100")
    private float confianza;

    @NotNull(message = "Factores influyentes es requerido")
    private JsonNode factoresInfluyentes;

    @NotNull(message = "La fecha de prediccion es requerida")
    private LocalDateTime fechaPrediccion;

    @NotBlank(message = "La version del modelo es requerida")
    private String modeloVersion;

    @NotNull(message = "El tipo de prediccion debe tener un valor válido")
    private TipoPrediccion tipo;

    private NivelRiesgo nivelRiesgo;

    private JsonNode recomendaciones;

    @Schema(readOnly = true)
    private String creadoPor;

    @Schema(readOnly = true)
    private String actualizadoPor;

    @Schema(readOnly = true)
    private LocalDateTime fechaCreacion;

    @Schema(readOnly = true)
    private LocalDateTime fechaActualizacion;
}
