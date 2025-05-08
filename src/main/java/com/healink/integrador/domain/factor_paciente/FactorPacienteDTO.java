package com.healink.integrador.domain.factor_paciente;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FactorPacienteDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El paciente id es requerido")
    private Long pacienteId;

    @NotNull(message = "El factor de riesgo id es requerido")
    private Long factorRiesgoId;

    @NotBlank(message = "El nombre de factor de riesgo es requerido")
    private String observacion;
}
