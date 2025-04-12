package com.healink.integrador.domain.factor_paciente;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FactorPacienteDTO implements DTOBase {

    private Long id;

    @NotBlank(message = "El paciente id es requerido")
    private Long pacienteId;

    @NotBlank(message = "El factor de riesgo id es requerido")
    private Long factorRiesgoId;

    @NotBlank(message = "El triaje id es requerido")
    private Long triajeId;

    // @NotBlank(message = "El nombre de factor de riesgo es requerido")
    // private String valor; //TODO: nombre muy generico, verificar

    @NotBlank(message = "La fecha de registro es requerida")
    private LocalDate fecha_registro;
}
