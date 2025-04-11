package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.service.ServicioGenerico;
// import com.healink.integrador.domain.servicios_medicos.ServicioMedico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServicioCampanaService extends ServicioGenerico<ServicioCampana> {

    private final ServicioCampanaRepository servicioCampanaRepository;

    public ServicioCampanaService(ServicioCampanaRepository servicioCampanaRepository) {
        super(servicioCampanaRepository);
        this.servicioCampanaRepository = servicioCampanaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<ServicioCampana>> findByCampanaId(Long campanaId) {
        return servicioCampanaRepository.findByCampanaId(campanaId);
    }
    @Transactional(readOnly = true)
    public Optional<List<ServicioCampana>> findByServicioId(Long servicioId) {
        return servicioCampanaRepository.findByServicioId(servicioId);
    }
}
