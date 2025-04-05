package com.healink.integrador.domain.seguimientos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

@Repository
public interface SeguimientoRepository extends RepositorioGenerico<Seguimiento> {

    List<Seguimiento> findByAtencionId(Long atencion_id);

    Page<Seguimiento> findByAtencionId(Long atencion_id, Pageable pageable);
}
