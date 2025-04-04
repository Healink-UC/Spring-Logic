package com.healink.integrador.domain.recomendaciones;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

@Repository
public interface RecomendacionRepository extends RepositorioGenerico<Recomendacion> {

    List<Recomendacion> findByDiagnosticoId(Long diagnostico_id);

    Page<Recomendacion> findByDiagnosticoId(Long diagnostico_id, Pageable pageable);
}
