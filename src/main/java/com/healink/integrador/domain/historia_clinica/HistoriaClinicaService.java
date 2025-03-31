package com.healink.integrador.domain.historia_clinica;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistoriaClinicaService extends ServicioGenerico<HistoriaClinica> {

    private final HistoriaClinicaRepository historiaClinicaRepository;

    public HistoriaClinicaService(HistoriaClinicaRepository historiaClinicaRepository) {
        super(historiaClinicaRepository);
        this.historiaClinicaRepository = historiaClinicaRepository;
    }

    @Transactional(readOnly = true)
    public List<HistoriaClinica> buscarPorPacienteId(Long pacienteId) {
        return historiaClinicaRepository.findByPacienteId(pacienteId);
    }

    @Transactional(readOnly = true)
    public Page<HistoriaClinica> buscarPorPacienteId(Long pacienteId, Pageable pageable) {
        return historiaClinicaRepository.findByPacienteId(pacienteId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<HistoriaClinica> buscarPorCitacionId(Long citacionId) {
        return historiaClinicaRepository.findByCitacionId(citacionId);
    }

    @Transactional(readOnly = true)
    public Optional<HistoriaClinica> buscarPorPacienteYTriajeId(Long pacienteId, Long triajeId) {
        return historiaClinicaRepository.findByPacienteIdAndTriajeId(pacienteId, triajeId);
    }

    @Transactional(readOnly = true)
    public List<HistoriaClinica> buscarPorRiesgoRehospitalizacion(Double umbral) {
        return historiaClinicaRepository.findByProbRehospitalizacionGreaterThanEqual(umbral);
    }
}
