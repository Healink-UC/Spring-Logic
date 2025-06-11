package com.healink.integrador.domain.predicciones;

import java.util.List;
import java.util.Optional;

import com.healink.integrador.core.Repository.RepositorioGenerico;

public interface PrediccionRepository extends RepositorioGenerico<Prediccion> {

    Optional<List<Prediccion>> findByPacienteId(Long pacienteId);
}
