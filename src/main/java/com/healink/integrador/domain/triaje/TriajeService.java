package com.healink.integrador.domain.triaje;

import org.springframework.stereotype.Service;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TriajeService extends ServicioGenerico<Triaje> {

    private final TriajeRepository triajeRepository;

    public TriajeService(TriajeRepository triajeRepository) {
        super(triajeRepository);
        this.triajeRepository = triajeRepository;
    }

    @Transactional
    public List<Triaje> findByPacienteId(Long pacienteId) {
        return triajeRepository.findByPacienteId(pacienteId);
    }
}
