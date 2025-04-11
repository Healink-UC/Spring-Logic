package com.healink.integrador.domain.factor_riesgo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healink.integrador.core.Repository.RepositorioGenerico;

public interface FactorRiesgoRepository extends RepositorioGenerico<FactorRiesgo> {
    List<FactorRiesgo> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT f FROM FactorRiesgo f WHERE LOWER(CAST(f.tipo AS string)) LIKE LOWER(CONCAT('%', :tipoTexto, '%'))")
    List<FactorRiesgo> findByTipoParcial(@Param("tipoTexto") String tipoTexto);

}
