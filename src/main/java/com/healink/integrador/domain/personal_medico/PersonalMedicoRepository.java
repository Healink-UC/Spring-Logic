package com.healink.integrador.domain.personal_medico;

import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalMedicoRepository extends RepositorioGenerico<PersonalMedico> {

    Optional<PersonalMedico> findByUsuarioId(Long usuarioId);

    List<PersonalMedico> findByEntidadId(Long entidadId);

    List<PersonalMedico> findByEstado(String estado);

    List<PersonalMedico> findByEspecialidad(String especialidad);
}