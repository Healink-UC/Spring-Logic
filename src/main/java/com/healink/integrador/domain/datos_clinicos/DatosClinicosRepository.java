package com.healink.integrador.domain.datos_clinicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DatosClinicosRepository extends RepositorioGenerico<DatosClinicos> {

    List<DatosClinicos> findByPacienteId(Long pacienteId);

    Page<DatosClinicos> findByPacienteId(Long pacienteId, Pageable pageable);

    List<DatosClinicos> findByPacienteIdAndFechaRegistroBetween(
            Long pacienteId, LocalDate fechaInicio, LocalDate fechaFin);
}