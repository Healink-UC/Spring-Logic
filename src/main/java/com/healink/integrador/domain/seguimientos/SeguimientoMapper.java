package com.healink.integrador.domain.seguimientos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface SeguimientoMapper extends MapeadorGenerico<Seguimiento, SeguimientoDTO> {
    @Override
    @Mapping(target = "atencion_id", source = "atencion.id") // Extracción de Rol a rolId
    SeguimientoDTO aDTO(Seguimiento entity);

    @Override
    @Mapping(target = "atencion.id", source = "atencion_id")
    Seguimiento aEntidad(SeguimientoDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "atencion.id", source = "atencion_id")
    void actualizarEntidadDesdeDTO(SeguimientoDTO dto, @MappingTarget Seguimiento entity);
}