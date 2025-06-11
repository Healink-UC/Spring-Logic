package com.healink.integrador.domain.usuario;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends RepositorioGenerico<Usuario> {

    Optional<Usuario> findByCorreo(String correo);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.rol LEFT JOIN FETCH u.entidadSalud WHERE u.tipoIdentificacion = :tipoIdentificacion AND u.identificacion = :identificacion")
    Optional<Usuario> findByTipoIdentificacionAndIdentificacion(
            @Param("tipoIdentificacion") TipoIdentificacion tipoIdentificacion,
            @Param("identificacion") String identificacion);

    boolean existsByCorreo(String correo);

    // // Obtener todos los usuarios con su entidad de salud (relación uno a uno)
    // @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.entidadSalud")
    // List<Usuario> findAllWithEntidadSalud();

    // // Obtener usuario por ID con entidad de salud cargada (relación uno a uno)
    // @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.entidadSalud WHERE u.id = :id")
    // Optional<Usuario> findByIdWithEntidadSalud(@Param("id") Long id);

}
