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

    private Long triajeId;

    private Long datosClinicosId;

    private Long citacionId;

    @PositiveOrZero(message = "La probabilidad de rehospitalización debe ser un valor positivo o cero")
    private Double probRehospitalizacion;
}
