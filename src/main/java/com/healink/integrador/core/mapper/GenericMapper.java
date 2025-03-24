package com.healink.integrador.core.mapper;

import java.util.List;

import com.healink.integrador.core.dto.BaseDTO;
import com.healink.integrador.core.entity.BaseEntity;
import org.mapstruct.MappingTarget;

public interface GenericMapper<E extends BaseEntity, D extends BaseDTO> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entities);

    List<E> toEntityList(List<D> dtos);

    void updateEntityFromDto(D dto, @MappingTarget E entity);
}