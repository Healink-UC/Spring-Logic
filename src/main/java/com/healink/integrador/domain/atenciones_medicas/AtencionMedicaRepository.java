package com.healink.integrador.domain.atenciones_medicas;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

@Repository
public interface AtencionMedicaRepository extends RepositorioGenerico<AtencionMedica> {

    Optional<AtencionMedica> findByCitacionId(Long citacion_id);

}
