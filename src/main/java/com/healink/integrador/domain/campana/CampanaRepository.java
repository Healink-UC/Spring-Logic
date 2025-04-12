package com.healink.integrador.domain.campana;

import java.util.List;

import com.healink.integrador.core.Repository.RepositorioGenerico;

public interface CampanaRepository extends RepositorioGenerico<Campana> {

    List<Campana> findAllByOrderByFechaInicioAsc();

    List<Campana> findAllByOrderByFechaLimiteInscripcionAsc();

}
