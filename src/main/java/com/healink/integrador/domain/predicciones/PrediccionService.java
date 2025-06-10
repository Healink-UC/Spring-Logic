package com.healink.integrador.domain.predicciones;

import java.util.List;

import org.springframework.stereotype.Service;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrediccionService extends ServicioGenerico<Prediccion> {

    private final PrediccionRepository prediccionRepository;

    public PrediccionService(PrediccionRepository prediccionesRepository) {
        super(prediccionesRepository);
        this.prediccionRepository = prediccionesRepository;
    }
    
    @Transactional
    public List<Prediccion> buscarPorPacienteYTipo(Long pacienteId, String tipoString) {
        try {
            TipoPrediccion tipo = TipoPrediccion.valueOf(tipoString.toUpperCase());
            return prediccionRepository.findByPacienteIdAndTipo(pacienteId, tipo);
        } catch (IllegalArgumentException e) {
            // Si el tipo no es válido, retornar lista vacía
            return List.of();
        }
    }
    
    @Transactional
    public List<Prediccion> buscarPorPaciente(Long pacienteId) {
        return prediccionRepository.findByPacienteIdOrderByFechaDesc(pacienteId);
    }

}