package com.healink.integrador.domain.predicciones;

import org.springframework.stereotype.Service;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrediccionesService extends ServicioGenerico<Predicciones> {

    public PrediccionesService(PrediccionesRepository prediccionesRepository) {
        super(prediccionesRepository);

    }

}
