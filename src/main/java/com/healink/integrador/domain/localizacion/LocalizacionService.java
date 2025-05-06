package com.healink.integrador.domain.localizacion;

import com.healink.integrador.core.service.ServicioGenerico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LocalizacionService extends ServicioGenerico<Localizacion> {

    private final LocalizacionRepository localizacionRepository;

    public LocalizacionService(LocalizacionRepository localizacionRepository) {
        super(localizacionRepository);
        this.localizacionRepository = localizacionRepository;
    }
}
