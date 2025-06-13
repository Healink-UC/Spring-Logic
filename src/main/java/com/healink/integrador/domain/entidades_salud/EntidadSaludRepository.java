package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntidadSaludRepository extends RepositorioGenerico<EntidadSalud> {
    Optional<EntidadSalud> findByRazonSocial(String razonSocial);

    Optional<EntidadSalud> findByUsuarioId(Long usuarioId);
    
    /**
     * Buscar entidades de salud por el administrador que las creó
     * @param creadoPor identificador del creador (formato: "TipoIdentificacion:Identificacion")
     * @return lista de entidades creadas por ese administrador
     */
    List<EntidadSalud> findByCreadoPor(String creadoPor);
}
