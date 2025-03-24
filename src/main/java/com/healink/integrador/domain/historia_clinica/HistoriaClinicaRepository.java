package com.healink.integrador.domain.historia_clinica;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends RepositorioGenerico<HistoriaClinica> {

    List<HistoriaClinica> findByPacienteId(Long pacienteId);

    Page<HistoriaClinica> findByPacienteId(Long pacienteId, Pageable pageable);

    Optional<HistoriaClinica> findByCitacionId(Long citacionId);

    Optional<HistoriaClinica> findByPacienteIdAndTriajeId(Long pacienteId, Long triajeId);

    List<HistoriaClinica> findByProbRehospitalizacionGreaterThanEqual(Double umbral);
}