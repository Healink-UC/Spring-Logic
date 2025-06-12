package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmbajadorRepository extends RepositorioGenerico<Embajador> {

    @Query("SELECT e FROM Embajador e LEFT JOIN FETCH e.usuario u LEFT JOIN FETCH u.rol WHERE e.usuarioId = :usuarioId")
    Optional<Embajador> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT e FROM Embajador e LEFT JOIN FETCH e.usuario u LEFT JOIN FETCH u.rol WHERE e.creadoPor = :creadoPor")
    List<Embajador> findByCreadoPor(@Param("creadoPor") String creadoPor);
}
