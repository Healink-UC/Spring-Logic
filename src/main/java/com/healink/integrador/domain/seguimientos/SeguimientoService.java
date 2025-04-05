package com.healink.integrador.domain.seguimientos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class SeguimientoService extends ServicioGenerico<Seguimiento> {

    private final SeguimientoRepository seguimientoRepository;

    public SeguimientoService(SeguimientoRepository seguimientoRepository) {
        super(seguimientoRepository);
        this.seguimientoRepository = seguimientoRepository;
    }

    @Override
    public Seguimiento guardar(Seguimiento seguimiento) {
        return super.guardar(seguimiento);
    }

    @Override
    public Seguimiento obtenerPorId(Long id) {
        return seguimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seguimiento no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Seguimiento> buscarPorAtencionId(Long atencion_id) {
        return seguimientoRepository.findByAtencionId(atencion_id);
    }

    @Transactional(readOnly = true)
    public Page<Seguimiento> buscarPorAtencionId(Long diagnostico_id, Pageable pageable) {
        return seguimientoRepository.findByAtencionId(diagnostico_id, pageable);
    }
}