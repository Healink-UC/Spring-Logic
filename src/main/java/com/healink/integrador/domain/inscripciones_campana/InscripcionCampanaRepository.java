package com.healink.integrador.domain.inscripciones_campana;

import org.springframework.stereotype.Repository;
import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionCampanaRepository extends RepositorioGenerico<InscripcionCampana> {
    List<InscripcionCampana> findByUsuarioId(Long usuarioId);

    List<InscripcionCampana> findByCampanaId(Long campanaId);

    Optional<InscripcionCampana> findByUsuarioIdAndCampanaId(Long usuarioId, Long campanaId);

    List<InscripcionCampana> findByUsuarioIdAndEstado(Long usuarioId, EstadoInscripcion estado);
}