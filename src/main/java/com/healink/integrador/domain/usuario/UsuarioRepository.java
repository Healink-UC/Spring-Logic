package com.healink.integrador.domain.usuario;

import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.GenericRepository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends GenericRepository<Usuario> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByTipoIdentificacionAndIdentificacion(String tipoIdentificacion,
            String identificacion);

    boolean existsByCorreo(String correo);
}
