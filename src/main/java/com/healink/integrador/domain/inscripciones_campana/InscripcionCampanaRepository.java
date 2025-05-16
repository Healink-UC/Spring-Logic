package com.healink.integrador.domain.inscripciones_campana;

import org.springframework.stereotype.Repository;
import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionCampanaRepository extends RepositorioGenerico<InscripcionCampana> {
    List<InscripcionCampana> findByPacienteId(Long pacienteId);
    List<InscripcionCampana> findByCampanaId(Long campanaId);
    Optional<InscripcionCampana> findByPacienteIdAndCampanaId(Long pacienteId, Long campanaId);
    List<InscripcionCampana> findByPacienteIdAndEstado(Long pacienteId, EstadoInscripcion estado);
} 