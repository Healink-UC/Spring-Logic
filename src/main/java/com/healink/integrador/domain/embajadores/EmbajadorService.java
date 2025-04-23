package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.service.ServicioGenerico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmbajadorService extends ServicioGenerico<Embajador> {

    private final EmbajadorRepository embajadorRepository;

    public EmbajadorService(EmbajadorRepository embajadorRepository) {
        super(embajadorRepository);
        this.embajadorRepository = embajadorRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<Embajador>> findByEntidad(Long entidadId) {
        return embajadorRepository.findByEntidadId(entidadId);
    }

    @Transactional(readOnly = true)
    public Optional<Embajador> findByUsuario(Long usuarioId) {
        return embajadorRepository.findByUsuarioId(usuarioId);
    }
}
