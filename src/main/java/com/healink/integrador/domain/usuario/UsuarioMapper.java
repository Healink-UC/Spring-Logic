package com.healink.integrador.domain.usuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import com.healink.integrador.domain.rol.Rol;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends MapeadorGenerico<Usuario, UsuarioDTO> {

    @Override
    @Mapping(target = "clave", ignore = true)
    UsuarioDTO aDTO(Usuario entity);

    @Override
    @Mapping(target = "rol.id", source = "rolId")
    Usuario aEntidad(UsuarioDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol.id", source = "rolId")
    void actualizarEntidadDesdeDTO(UsuarioDTO dto, @MappingTarget Usuario entity);

}
