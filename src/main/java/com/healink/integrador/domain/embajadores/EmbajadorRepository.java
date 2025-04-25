package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;
import java.util.Optional;

public interface EmbajadorRepository extends RepositorioGenerico<Embajador> {
    Optional<List<Embajador>> findByEntidadId(Long entidadId);

    Optional<Embajador> findByUsuarioId(Long usuarioId);
}
