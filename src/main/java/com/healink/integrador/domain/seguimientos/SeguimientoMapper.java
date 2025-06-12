package com.healink.integrador.domain.seguimientos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface SeguimientoMapper extends MapeadorGenerico<Seguimiento, SeguimientoDTO> {
    @Override
    @Mapping(target = "citacion_id", source = "citacion.id") // Extracción de Citacion a citacionId
    SeguimientoDTO aDTO(Seguimiento entity);

    @Override
    @Mapping(target = "citacion.id", source = "citacion_id")
    Seguimiento aEntidad(SeguimientoDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "citacion.id", source = "citacion_id")
    void actualizarEntidadDesdeDTO(SeguimientoDTO dto, @MappingTarget Seguimiento entity);
}