package com.healink.integrador.domain.predicciones;

import java.util.List;

import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import com.healink.integrador.core.service.ServicioGenerico;


@Service
@Transactional
public class PrediccionService extends ServicioGenerico<Prediccion> {

    private final PrediccionRepository prediccionesRepository;

    public PrediccionService(PrediccionRepository prediccionesRepository) {
        super(prediccionesRepository);
        this.prediccionesRepository = prediccionesRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<Prediccion>> buscarPorPacienteId(Long pacienteId) {
        return prediccionesRepository.findByPacienteId(pacienteId);
    }

    @Transactional(readOnly = true)
    public List<Prediccion> buscarPorPacienteYTipo(Long pacienteId, String tipoString) {
        try {
            TipoPrediccion tipo = TipoPrediccion.valueOf(tipoString.toUpperCase());
            return prediccionesRepository.findByPacienteIdAndTipo(pacienteId, tipo);
        } catch (IllegalArgumentException e) {
            // Si el tipo no es válido, retornar lista vacía
            return List.of();
        }
    }
}