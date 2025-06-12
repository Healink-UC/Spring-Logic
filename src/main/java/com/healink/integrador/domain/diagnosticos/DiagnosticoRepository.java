package com.healink.integrador.domain.diagnosticos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

@Repository
public interface DiagnosticoRepository extends RepositorioGenerico<Diagnostico> {

    List<Diagnostico> findByCitacionId(Long citacion_id);

    Page<Diagnostico> findByCitacionId(Long citacion_id, Pageable pageable);

    List<Diagnostico> findByCodigoCie10(String codigo_cie10);

    Page<Diagnostico> findByCodigoCie10(String codigo_cie10, Pageable pageable);
}
