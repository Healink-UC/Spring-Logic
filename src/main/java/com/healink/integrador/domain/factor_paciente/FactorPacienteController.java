package com.healink.integrador.domain.factor_paciente;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/factor-paciente")
@Tag(name = "Factores paciente")
public class FactorPacienteController extends ControladorGenerico<FactorPaciente, FactorPacienteDTO> {

    public FactorPacienteController(FactorPacienteService factorPacienteService,
            FactorPacienteMapper factorPacienteMapper) {
        super(factorPacienteService, factorPacienteMapper);
    }

}
