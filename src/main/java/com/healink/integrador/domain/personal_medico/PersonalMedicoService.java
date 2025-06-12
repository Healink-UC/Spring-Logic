package com.healink.integrador.domain.personal_medico;

import com.healink.integrador.core.service.ServicioGenerico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonalMedicoService extends ServicioGenerico<PersonalMedico> {

    private final PersonalMedicoRepository personalMedicoRepository;

    public PersonalMedicoService(PersonalMedicoRepository personalMedicoRepository) {
        super(personalMedicoRepository);
        this.personalMedicoRepository = personalMedicoRepository;
    }

    @Transactional(readOnly = true)
    public Optional<PersonalMedico> findByUsuario(Long usuarioId) {
        return personalMedicoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<PersonalMedico> findByEntidad(Long entidadId) {
        return personalMedicoRepository.findByEntidadId(entidadId);
    }

    @Transactional(readOnly = true)
    public List<PersonalMedico> findByEspecialidad(String especialidad) {
        return personalMedicoRepository.findByEspecialidad(especialidad);
    }

    @Transactional(readOnly = true)
    public List<PersonalMedico> findByEntidadAndEspecialidad(Long entidadId, String especialidad) {
        return personalMedicoRepository.findByEntidadIdAndEspecialidad(entidadId, especialidad);
    }
} 