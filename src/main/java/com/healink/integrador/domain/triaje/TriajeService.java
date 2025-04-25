package com.healink.integrador.domain.triaje;

import org.springframework.stereotype.Service;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TriajeService extends ServicioGenerico<Triaje> {

    public TriajeService(TriajeRepository triajeRepository) {
        super(triajeRepository);

    }

}
