package com.healink.integrador.domain.diagnosticos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class DiagnosticoService extends ServicioGenerico<Diagnostico> {

    private final DiagnosticoRepository diagnosticoRepository;

    public DiagnosticoService(DiagnosticoRepository diagnosticoRepository) {
        super(diagnosticoRepository);
        this.diagnosticoRepository = diagnosticoRepository;
    }

    @Override
    public Diagnostico guardar(Diagnostico diagnostico) {
        return super.guardar(diagnostico);
    }

    @Override
    public Diagnostico obtenerPorId(Long id) {
        return diagnosticoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diagnóstico no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Diagnostico> buscarPorAtencionId(Long atencion_id) {
        return diagnosticoRepository.findByAtencionId(atencion_id);
    }

    @Transactional(readOnly = true)
    public Page<Diagnostico> buscarPorAtencionId(Long atencion_id, Pageable pageable) {
        return diagnosticoRepository.findByAtencionId(atencion_id, pageable);
    }

    @Transactional(readOnly = true)
    public List<Diagnostico> buscarPorCodigoCie10(String codigo_cie10) {
        return diagnosticoRepository.findByCodigoCie10(codigo_cie10);
    }

    @Transactional(readOnly = true)
    public Page<Diagnostico> buscarPorCodigoCie10(String codigo_cie10, Pageable pageable) {
        return diagnosticoRepository.findByCodigoCie10(codigo_cie10, pageable);
    }
}