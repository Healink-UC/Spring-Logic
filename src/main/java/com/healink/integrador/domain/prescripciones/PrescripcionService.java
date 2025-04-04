package com.healink.integrador.domain.prescripciones;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PrescripcionService extends ServicioGenerico<Prescripcion> {

    private final PrescripcionRepository prescripcionRepository;

    public PrescripcionService(PrescripcionRepository prescripcionRepository) {
        super(prescripcionRepository);
        this.prescripcionRepository = prescripcionRepository;
    }

    @Override
    public Prescripcion guardar(Prescripcion prescripcion) {
        return super.guardar(prescripcion);
    }

    @Override
    public Prescripcion obtenerPorId(Long id) {
        return prescripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prescripción no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Prescripcion> buscarPorDiagnosticoId(Long diagnostico_id) {
        return prescripcionRepository.findByDiagnosticoId(diagnostico_id);
    }

    @Transactional(readOnly = true)
    public Page<Prescripcion> buscarPorDiagnosticoId(Long diagnostico_id, Pageable pageable) {
        return prescripcionRepository.findByDiagnosticoId(diagnostico_id, pageable);
    }
}