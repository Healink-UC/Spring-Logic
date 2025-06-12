package com.healink.integrador.domain.seguimientos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

@Repository
public interface SeguimientoRepository extends RepositorioGenerico<Seguimiento> {

    List<Seguimiento> findByCitacionId(Long citacion_id);

    Page<Seguimiento> findByCitacionId(Long citacion_id, Pageable pageable);
}
