package com.healink.integrador.domain.triaje;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import java.util.List;

public interface TriajeRepository extends RepositorioGenerico<Triaje> {
    List<Triaje> findByPacienteId(Long pacienteId);
}
