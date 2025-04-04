package com.healink.integrador.domain.recomendaciones;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class RecomendacionService extends ServicioGenerico<Recomendacion> {

    private final RecomendacionRepository recomendacionRepository;

    public RecomendacionService(RecomendacionRepository recomendacionRepository) {
        super(recomendacionRepository);
        this.recomendacionRepository = recomendacionRepository;
    }

    @Override
    public Recomendacion guardar(Recomendacion recomendacion) {
        return super.guardar(recomendacion);
    }

    @Override
    public Recomendacion obtenerPorId(Long id) {
        return recomendacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recomendación no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Recomendacion> buscarPorDiagnosticoId(Long diagnostico_id) {
        return recomendacionRepository.findByDiagnosticoId(diagnostico_id);
    }

    @Transactional(readOnly = true)
    public Page<Recomendacion> buscarPorDiagnosticoId(Long diagnostico_id, Pageable pageable) {
        return recomendacionRepository.findByDiagnosticoId(diagnostico_id, pageable);
    }
}