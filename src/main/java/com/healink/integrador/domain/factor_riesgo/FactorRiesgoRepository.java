package com.healink.integrador.domain.factor_riesgo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FactorRiesgoRepository extends JpaRepository<FactorRiesgo, Long> {
    FactorRiesgo findByNombre(String nombre);

    FactorRiesgo findByTipo(String tipoFactorRiesgo);

}
