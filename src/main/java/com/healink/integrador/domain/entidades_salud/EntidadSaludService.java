package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.service.ServicioGenerico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EntidadSaludService extends ServicioGenerico<EntidadSalud> {

    private final EntidadSaludRepository entidadSaludRepository;

    public EntidadSaludService(EntidadSaludRepository entidadSaludRepository) {
        super(entidadSaludRepository);
        this.entidadSaludRepository = entidadSaludRepository;
    }

    @Transactional(readOnly = true)
    public Optional<EntidadSalud> findByRazonSocial(String razon) {
        return entidadSaludRepository.findByRazonSocial(razon);
    }

    @Transactional(readOnly = true)
    public Optional<EntidadSalud> findByUsuarioId(Long usuarioId) {
        return entidadSaludRepository.findByUsuarioId(usuarioId);
    }

}
