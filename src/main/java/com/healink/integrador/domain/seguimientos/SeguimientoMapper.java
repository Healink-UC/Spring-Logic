package com.healink.integrador.domain.seguimientos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface SeguimientoMapper extends MapeadorGenerico<Seguimiento, SeguimientoDTO> {
    
    @Override
    @Mapping(target = "citacionId", source = "citacion.id")
    SeguimientoDTO aDTO(Seguimiento entity);

}