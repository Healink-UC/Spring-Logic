package com.healink.integrador.domain.factor_riesgo;

import java.util.List;

import com.healink.integrador.core.Repository.RepositorioGenerico;

public interface FactorRiesgoRepository extends RepositorioGenerico<FactorRiesgo> {
    List<FactorRiesgo> findByNombre(String nombre);

    List<FactorRiesgo> findByTipo(String tipoFactorRiesgo);

}
