package com.healink.integrador.domain.inscripciones_campana;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class InscripcionCampanaService extends ServicioGenerico<InscripcionCampana> {

    private final InscripcionCampanaRepository inscripcionCampanaRepository;

    public InscripcionCampanaService(InscripcionCampanaRepository inscripcionCampanaRepository) {
        super(inscripcionCampanaRepository);
        this.inscripcionCampanaRepository = inscripcionCampanaRepository;
    }

    @Override
    public InscripcionCampana guardar(InscripcionCampana inscripcion) {
        return super.guardar(inscripcion);
    }

    @Override
    public InscripcionCampana obtenerPorId(Long id) {
        return inscripcionCampanaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<InscripcionCampana> buscarPorUsuarioId(Long usuarioId) {
        return inscripcionCampanaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<InscripcionCampana> buscarPorCampanaId(Long campanaId) {
        return inscripcionCampanaRepository.findByCampanaId(campanaId);
    }

    @Transactional(readOnly = true)
    public List<InscripcionCampana> buscarInscripcionesActivas(Long usuarioId) {
        return inscripcionCampanaRepository.findByUsuarioIdAndEstado(usuarioId, EstadoInscripcion.INSCRITO);
    }

    @Transactional
    public InscripcionCampana crearInscripcion(InscripcionCampana inscripcion) {
        // Establecer valores automáticos
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado(EstadoInscripcion.INSCRITO);
        
        // Limpiar campos que no deben ser establecidos en creación
        inscripcion.setMotivoRetiro(null);
        
        return guardar(inscripcion);
    }

    @Transactional
    public InscripcionCampana retirarInscripcion(Long id, String motivo) {
        InscripcionCampana inscripcion = obtenerPorId(id);
        
        if (inscripcion.getEstado() == EstadoInscripcion.RETIRADO) {
            throw new RuntimeException("La inscripción ya está retirada");
        }
        
        inscripcion.setEstado(EstadoInscripcion.RETIRADO);
        inscripcion.setMotivoRetiro(motivo);
        
        return guardar(inscripcion);
    }
}