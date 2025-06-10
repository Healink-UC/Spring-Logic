package com.healink.integrador.domain.predicciones;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healink.integrador.core.Repository.RepositorioGenerico;

public interface PrediccionRepository extends RepositorioGenerico<Prediccion> {

    @Query("SELECT p FROM Prediccion p WHERE p.pacienteId = :pacienteId AND p.tipo = :tipo ORDER BY p.fechaPrediccion DESC")
    List<Prediccion> findByPacienteIdAndTipo(@Param("pacienteId") Long pacienteId, @Param("tipo") TipoPrediccion tipo);
    
    @Query("SELECT p FROM Prediccion p WHERE p.pacienteId = :pacienteId ORDER BY p.fechaPrediccion DESC")
    List<Prediccion> findByPacienteIdOrderByFechaDesc(@Param("pacienteId") Long pacienteId);

}
