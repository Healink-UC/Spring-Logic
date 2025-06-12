package com.healink.integrador.domain.personal_medico;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalMedicoRepository extends RepositorioGenerico<PersonalMedico> {

    @Query("SELECT pm FROM PersonalMedico pm LEFT JOIN FETCH pm.usuario u LEFT JOIN FETCH u.rol LEFT JOIN FETCH pm.entidadSalud e")
    List<PersonalMedico> findAll();

    @Query("SELECT pm FROM PersonalMedico pm LEFT JOIN FETCH pm.usuario u LEFT JOIN FETCH u.rol LEFT JOIN FETCH pm.entidadSalud e WHERE pm.usuarioId = :usuarioId")
    Optional<PersonalMedico> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT pm FROM PersonalMedico pm LEFT JOIN FETCH pm.usuario u LEFT JOIN FETCH u.rol LEFT JOIN FETCH pm.entidadSalud e WHERE pm.entidadId = :entidadId")
    List<PersonalMedico> findByEntidadId(@Param("entidadId") Long entidadId);

    @Query("SELECT pm FROM PersonalMedico pm LEFT JOIN FETCH pm.usuario u LEFT JOIN FETCH u.rol LEFT JOIN FETCH pm.entidadSalud e WHERE pm.especialidad = :especialidad")
    List<PersonalMedico> findByEspecialidad(@Param("especialidad") String especialidad);

    @Query("SELECT pm FROM PersonalMedico pm LEFT JOIN FETCH pm.usuario u LEFT JOIN FETCH u.rol LEFT JOIN FETCH pm.entidadSalud e WHERE pm.entidadId = :entidadId AND pm.especialidad = :especialidad")
    List<PersonalMedico> findByEntidadIdAndEspecialidad(@Param("entidadId") Long entidadId, @Param("especialidad") String especialidad);
} 