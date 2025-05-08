package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.service.ServicioGenerico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CitacionMedicaService extends ServicioGenerico<CitacionMedica> {

    private final CitacionMedicaRepository citacionMedicaRepository;

    public CitacionMedicaService(CitacionMedicaRepository citacionMedicaRepository) {
        super(citacionMedicaRepository);
        this.citacionMedicaRepository = citacionMedicaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<CitacionMedica>> getByPacienteId(Long pacienteId) {
        return citacionMedicaRepository.findByPacienteId(pacienteId);
    }

    @Transactional(readOnly = true)
    public Optional<List<CitacionMedica>> getByMedicoId(Long medicoId) {
        return citacionMedicaRepository.findByMedicoId(medicoId);
    }

    @Transactional(readOnly = true)
    public Optional<List<CitacionMedica>> getByCampanaId(Long campanaId) {
        return citacionMedicaRepository.findByCampanaId(campanaId);
    }
}