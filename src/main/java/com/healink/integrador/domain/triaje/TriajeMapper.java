package com.healink.integrador.domain.triaje;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface TriajeMapper extends MapeadorGenerico<Triaje, TriajeDTO> {

}
