package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntidadSaludRepository extends RepositorioGenerico<EntidadSalud> {
    Optional<EntidadSalud> findByRazonSocial(String razonSocial);

    Optional<EntidadSalud> findByUsuarioId(Long usuarioId);
}
