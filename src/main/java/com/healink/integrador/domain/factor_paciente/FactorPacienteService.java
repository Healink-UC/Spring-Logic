package com.healink.integrador.domain.factor_paciente;

import org.springframework.stereotype.Service;

import com.healink.integrador.core.service.ServicioGenerico;

@Service
public class FactorPacienteService extends ServicioGenerico<FactorPaciente> {

    public FactorPacienteService(FactorPacienteRepository factorPacienteRepository) {
        super(factorPacienteRepository);
    }
}
