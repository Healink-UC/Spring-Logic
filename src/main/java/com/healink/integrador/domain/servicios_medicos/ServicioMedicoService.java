package com.healink.integrador.domain.servicios_medicos;

import com.healink.integrador.core.service.ServicioGenerico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioMedicoService extends ServicioGenerico<ServicioMedico> {

    private final ServicioMedicoRepository servicioMedicoRepository;

    public ServicioMedicoService(ServicioMedicoRepository servicioMedicoRepository) {
        super(servicioMedicoRepository);
        this.servicioMedicoRepository = servicioMedicoRepository;
    }
}
