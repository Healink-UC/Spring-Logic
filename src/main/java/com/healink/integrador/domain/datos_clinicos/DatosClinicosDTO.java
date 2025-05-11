package com.healink.integrador.domain.datos_clinicos;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DatosClinicosDTO implements DTOBase {

    @Schema(readOnly = true, description = "Identificador único de los datos clínicos")
    private Long id;

    @NotNull(message = "El ID de paciente es requerido")
    private Long pacienteId;

    @NotNull(message = "La presión sistólica es requerida")
    @PositiveOrZero(message = "La presión sistólica debe ser un valor positivo o cero")
    @Max(value = 300, message = "La presión sistólica no puede ser mayor a 300 mmHg")
    private Double presionSistolica;

    @NotNull(message = "La presión diastólica es requerida")
    @PositiveOrZero(message = "La presión diastólica debe ser un valor positivo o cero")
    @Max(value = 200, message = "La presión diastólica no puede ser mayor a 200 mmHg")
    private Double presionDiastolica;

    @PositiveOrZero(message = "La frecuencia cardíaca mínima debe ser un valor positivo o cero")
    @Max(value = 300, message = "La frecuencia cardíaca mínima no puede ser mayor a 300 lpm")
    private Double frecuenciaCardiacaMin;

    @PositiveOrZero(message = "La frecuencia cardíaca máxima debe ser un valor positivo o cero")
    @Max(value = 300, message = "La frecuencia cardíaca máxima no puede ser mayor a 300 lpm")
    private Double frecuenciaCardiacaMax;

    @PositiveOrZero(message = "La saturación de oxígeno debe ser un valor positivo o cero")
    @Max(value = 100, message = "La saturación de oxígeno no puede ser mayor a 100%")
    private Double saturacionOxigeno;

    @PositiveOrZero(message = "La temperatura debe ser un valor positivo o cero")
    @Max(value = 45, message = "La temperatura no puede ser mayor a 45°C")
    private Double temperatura;

    @NotNull(message = "El colesterol total es requerido")
    @PositiveOrZero(message = "El colesterol total debe ser un valor positivo o cero")
    @Max(value = 1000, message = "El colesterol total no puede ser mayor a 1000 mg/dL")
    private Double colesterolTotal;

    @NotNull(message = "El HDL es requerido")
    @PositiveOrZero(message = "El HDL debe ser un valor positivo o cero")
    @Max(value = 300, message = "El HDL no puede ser mayor a 300 mg/dL")
    private Double hdl;

    private String observaciones;

    @NotNull(message = "La fecha de medición es requerida")
    private LocalDate fechaMedicion;
}