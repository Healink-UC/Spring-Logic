package com.healink.integrador.domain.rol;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.json.JsonMapper;
import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring", uses = { JsonMapper.class })
public interface RolMapper extends MapeadorGenerico<Rol, RolDTO> {
    @Override
    @Mapping(target = "permisos", source = "permisos")
    RolDTO aDTO(Rol entity);

    @Override
    @Mapping(target = "permisos", source = "permisos")
    Rol aEntidad(RolDTO dto);

    // No actualizar el ID
    @Override
    @Mapping(target = "id", ignore = true)
    void actualizarEntidadDesdeDTO(RolDTO dto, @MappingTarget Rol entity);
}
