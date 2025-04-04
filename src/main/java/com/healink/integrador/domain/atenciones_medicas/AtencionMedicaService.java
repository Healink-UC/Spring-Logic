package com.healink.integrador.domain.atenciones_medicas;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class AtencionMedicaService extends ServicioGenerico<AtencionMedica> {

    private final AtencionMedicaRepository atencionMedicaRepository;

    public AtencionMedicaService(AtencionMedicaRepository atencionMedicaRepository) {
        super(atencionMedicaRepository);
        this.atencionMedicaRepository = atencionMedicaRepository;
    }

    @Override
    public AtencionMedica guardar(AtencionMedica atencionMedica) {
        return super.guardar(atencionMedica);
    }

    @Override
    public AtencionMedica obtenerPorId(Long id) {
        return atencionMedicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atención Médica no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<AtencionMedica> buscarPorCitacionId(Long citacion_id) {
        return atencionMedicaRepository.findByCitacionId(citacion_id);
    }
}