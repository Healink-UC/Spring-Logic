package com.healink.integrador.domain.predicciones;

import org.springframework.stereotype.Service;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrediccionService extends ServicioGenerico<Prediccion> {

    public PrediccionService(PrediccionRepository prediccionesRepository) {
        super(prediccionesRepository);
    }

}