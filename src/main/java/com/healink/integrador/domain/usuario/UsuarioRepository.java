package com.healink.integrador.domain.usuario;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends RepositorioGenerico<Usuario> {

    Optional<Usuario> findByCorreo(String correo);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.rol WHERE u.tipoIdentificacion = :tipoIdentificacion AND u.identificacion = :identificacion")
    Optional<Usuario> findByTipoIdentificacionAndIdentificacion(
            @Param("tipoIdentificacion") TipoIdentificacion tipoIdentificacion,
            @Param("identificacion") String identificacion);

    boolean existsByCorreo(String correo);
}
