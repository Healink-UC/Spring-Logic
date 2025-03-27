package com.healink.integrador.domain.usuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends MapeadorGenerico<Usuario, UsuarioDTO> {

    @Override
    @Mapping(target = "clave", ignore = true)
    UsuarioDTO aDTO(Usuario dto);

    @Override
    void actualizarEntidadDesdeDto(UsuarioDTO dto, @MappingTarget Usuario entidad);

}
