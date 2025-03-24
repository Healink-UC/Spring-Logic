package com.healink.integrador.domain.usuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.GenericMapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends GenericMapper<Usuario, UsuarioDTO> {

    @Override
    @Mapping(target = "clave", ignore = true)
    UsuarioDTO toDto(Usuario entity);

    @Override
    void updateEntityFromDto(UsuarioDTO dto, @MappingTarget Usuario entity);

}
