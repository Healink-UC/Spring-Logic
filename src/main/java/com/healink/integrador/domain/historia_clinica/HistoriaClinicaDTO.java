package com.healink.integrador.domain.historia_clinica;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class HistoriaClinicaDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El ID de paciente es requerido")
    private Long pacienteId;

    @NotNull(message = "El ID del triaje es requerido")
    private Long triajeId;

    @NotNull(message = "El ID de los datos clínicos es requerido")
    private Long datosClinicosId;

    @NotNull(message = "El ID del diagnóstico es requerido")
    private Long diagnosticoId;

    @NotNull(message = "El ID de la recomendación es requerido")
    private Long recomendacionId;

    @NotNull(message = "El ID del seguimiento es requerido")
    private Long seguimientoId;

    @NotNull(message = "El ID de la prescripción es requerido")
    private Long prescripcionId;

    @PositiveOrZero(message = "La probabilidad de rehospitalización debe ser un valor positivo o cero")
    private Double probRehospitalizacion;
}