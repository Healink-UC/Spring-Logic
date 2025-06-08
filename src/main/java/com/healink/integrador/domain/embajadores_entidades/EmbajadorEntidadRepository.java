package com.healink.integrador.domain.embajadores_entidades;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmbajadorEntidadRepository extends RepositorioGenerico<EmbajadorEntidad> {
    
    @Query("SELECT ee FROM EmbajadorEntidad ee " +
           "LEFT JOIN FETCH ee.embajador e " +
           "LEFT JOIN FETCH e.usuario u " +
           "LEFT JOIN FETCH u.rol " +
           "LEFT JOIN FETCH ee.entidadSalud " +
           "WHERE ee.entidadId = :entidadId")
    List<EmbajadorEntidad> findByEntidadId(@Param("entidadId") Long entidadId);

    @Query("SELECT ee FROM EmbajadorEntidad ee " +
           "LEFT JOIN FETCH ee.embajador e " +
           "LEFT JOIN FETCH e.usuario u " +
           "LEFT JOIN FETCH u.rol " +
           "LEFT JOIN FETCH ee.entidadSalud " +
           "WHERE ee.embajadorId = :embajadorId")
    List<EmbajadorEntidad> findByEmbajadorId(@Param("embajadorId") Long embajadorId);

    @Query("SELECT ee FROM EmbajadorEntidad ee " +
           "LEFT JOIN FETCH ee.embajador e " +
           "LEFT JOIN FETCH e.usuario u " +
           "LEFT JOIN FETCH u.rol " +
           "LEFT JOIN FETCH ee.entidadSalud " +
           "WHERE ee.embajadorId = :embajadorId AND ee.entidadId = :entidadId")
    Optional<EmbajadorEntidad> findByEmbajadorIdAndEntidadId(@Param("embajadorId") Long embajadorId, @Param("entidadId") Long entidadId);
} 