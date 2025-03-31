package com.healink.integrador.domain.personal_medico;

import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

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

    @Override
    public PersonalMedico guardar(PersonalMedico personalMedico) {
        // Validar que no exista otro personal médico con el mismo usuario
        if (personalMedico.getId() == null &&
                personalMedicoRepository.findByUsuarioId(personalMedico.getUsuarioId()).isPresent()) {
            throw new EntityExistsException(
                    "Ya existe un médico para el usuario con ID: " + personalMedico.getUsuarioId());
        }

        return super.guardar(personalMedico);
    }

    @Transactional(readOnly = true)
    public Optional<PersonalMedico> buscarPorUsuarioId(Long usuarioId) {
        return personalMedicoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<PersonalMedico> buscarPorEntidadId(Long entidadId) {
        return personalMedicoRepository.findByEntidadId(entidadId);
    }

    @Transactional(readOnly = true)
    public List<PersonalMedico> buscarPorEstado(String estado) {
        return personalMedicoRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<PersonalMedico> buscarPorEspecialidad(String especialidad) {
        return personalMedicoRepository.findByEspecialidad(especialidad);
    }
}