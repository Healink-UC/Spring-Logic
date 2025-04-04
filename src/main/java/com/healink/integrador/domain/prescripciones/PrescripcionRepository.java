package com.healink.integrador.domain.prescripciones;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

@Repository
public interface PrescripcionRepository extends RepositorioGenerico<Prescripcion> {

    List<Prescripcion> findByDiagnosticoId(Long diagnostico_id);

    Page<Prescripcion> findByDiagnosticoId(Long diagnostico_id, Pageable pageable);
}
