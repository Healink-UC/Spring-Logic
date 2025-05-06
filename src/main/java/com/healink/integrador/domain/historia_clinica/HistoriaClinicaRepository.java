package com.healink.integrador.domain.historia_clinica;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;

@Repository
public interface HistoriaClinicaRepository extends RepositorioGenerico<HistoriaClinica> {

    List<HistoriaClinica> findByPacienteId(Long pacienteId);

    Page<HistoriaClinica> findByPacienteId(Long pacienteId, Pageable pageable);

    List<HistoriaClinica> findByProbRehospitalizacionGreaterThanEqual(Double umbral);
}