package com.healink.integrador.domain.datos_clinicos;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class DatosClinicosDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El ID de paciente es requerido")
    private Long pacienteId;

    @PositiveOrZero(message = "La presión sistólica debe ser un valor positivo o cero")
    private Double presionSistolica;

    @PositiveOrZero(message = "La presión diastólica debe ser un valor positivo o cero")
    private Double presionDiastolica;

    @PositiveOrZero(message = "La frecuencia cardíaca mínima debe ser un valor positivo o cero")
    private Double frecuenciaCardiacaMin;

    @PositiveOrZero(message = "La frecuencia cardíaca máxima debe ser un valor positivo o cero")
    private Double frecuenciaCardiacaMax;

    @PositiveOrZero(message = "La saturación de oxígeno debe ser un valor positivo o cero")
    private Double saturacionOxigeno;

    @PositiveOrZero(message = "La temperatura debe ser un valor positivo o cero")
    private Double temperatura;

    @Positive(message = "El peso debe ser un valor positivo")
    private Double peso;

    @Positive(message = "La talla debe ser un valor positivo")
    private Double talla;

    private Double imc;

    private String observaciones;
}