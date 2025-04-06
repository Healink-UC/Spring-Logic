package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import com.healink.integrador.domain.entidades_salud.EntidadSalud;

import java.util.List;
import java.util.Optional;

public interface EmbajadorRepository extends RepositorioGenerico<Embajador> {
    Optional<List<Embajador>> findByEntidad(Long entidadId);
    Optional<Embajador> findByUsuario(Long usuarioId);
}
