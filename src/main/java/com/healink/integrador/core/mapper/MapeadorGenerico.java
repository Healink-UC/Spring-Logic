package com.healink.integrador.core.mapper;

import java.util.List;

import com.healink.integrador.core.dto.DTOBase;
import com.healink.integrador.core.entity.EntidadBase;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface MapeadorGenerico<E extends EntidadBase, D extends DTOBase> {

    D aDTO(E entity);

    E aEntidad(D dto);

    List<D> aListaDTO(List<E> entities);

    List<E> aListaEntidad(List<D> dtos);

    @Mapping(target = "id", ignore = true)
    void actualizarEntidadDesdeDTO(D dto, @MappingTarget E entity);
}