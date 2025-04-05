package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.Optional;

public interface EntidadSaludRepository extends RepositorioGenerico<EntidadSalud> {
    Optional<EntidadSalud> findByRazonSocial(String razonSocial);
    Optional<EntidadSalud> findByUsuario(Long usuarioId);
}
