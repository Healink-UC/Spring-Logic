package com.healink.integrador.domain.rol;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface RolMapper extends MapeadorGenerico<Rol, RolDTO> {
}
