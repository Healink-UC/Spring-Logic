package com.healink.integrador.domain.paciente;

import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.GenericRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends GenericRepository<Paciente> {

    List<Paciente> findByLocalidad(String localidad);

    Optional<Paciente> findByUsuarioId(Long usuarioId);
}