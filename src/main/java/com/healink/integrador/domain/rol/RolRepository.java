package com.healink.integrador.domain.rol;

import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.Optional;

@Repository
public interface RolRepository extends RepositorioGenerico<Rol> {

    Optional<Rol> findByNombre(String nombre);
}
