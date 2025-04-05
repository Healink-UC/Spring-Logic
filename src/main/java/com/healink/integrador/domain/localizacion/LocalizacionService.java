package com.healink.integrador.domain.localizacion;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import com.healink.integrador.core.service.ServicioGenerico;
import com.healink.integrador.domain.localizacion.Localizacion;
import com.healink.integrador.domain.paciente.Paciente;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocalizacionService extends ServicioGenerico<Localizacion> {

    private final LocalizacionRepository localizacionRepository;

    public LocalizacionService(LocalizacionRepository localizacionRepository) {
        super(localizacionRepository);
        this.localizacionRepository = localizacionRepository;
    }
}
