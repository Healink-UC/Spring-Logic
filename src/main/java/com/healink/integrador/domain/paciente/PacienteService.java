package com.healink.integrador.domain.paciente;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.GenericService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PacienteService extends GenericService<Paciente> {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        super(pacienteRepository);
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Paciente> findByLocalidad(String localidad) {
        return pacienteRepository.findByLocalidad(localidad);
    }

    @Transactional(readOnly = true)
    public Optional<Paciente> findByUsuarioId(Long usuarioId) {
        return pacienteRepository.findByUsuarioId(usuarioId);
    }
}