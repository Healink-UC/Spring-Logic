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

    /**
     * Buscar personal médico por ID con relaciones lazy cargadas
     * Este método soluciona el LazyInitializationException
     */
    @Transactional(readOnly = true)
    public Optional<PersonalMedico> findByIdWithRelations(Long id) {
        Optional<PersonalMedico> personalMedico = personalMedicoRepository.findById(id);
        
        if (personalMedico.isPresent()) {
            PersonalMedico pm = personalMedico.get();
            // Forzar la carga de las relaciones lazy
            if (pm.getUsuario() != null) {
                pm.getUsuario().getNombres(); // Trigger lazy loading
            }
            if (pm.getEntidadSalud() != null) {
                pm.getEntidadSalud().getRazonSocial(); // Trigger lazy loading
            }
        }
        
        return personalMedico;
    }
} 