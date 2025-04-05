package com.healink.integrador.domain.usuario;

import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import com.healink.integrador.enums.TipoIdentificacion;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends RepositorioGenerico<Usuario> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByTipoIdentificacionAndIdentificacion(
            TipoIdentificacion tipoIdentificacion,
            String identificacion);

    boolean existsByCorreo(String correo);
}
