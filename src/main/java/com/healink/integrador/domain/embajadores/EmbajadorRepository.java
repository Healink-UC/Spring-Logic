package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmbajadorRepository extends RepositorioGenerico<Embajador> {
    
    //@Query("SELECT e FROM Embajador e LEFT JOIN FETCH e.usuario u LEFT JOIN FETCH u.rol WHERE e.entidadId = :entidadId")
    //Optional<List<Embajador>> findByEntidadId(@Param("entidadId") Long entidadId);

    @Query("SELECT e FROM Embajador e LEFT JOIN FETCH e.usuario u LEFT JOIN FETCH u.rol WHERE e.usuarioId = :usuarioId")
    Optional<Embajador> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
