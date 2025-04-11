package com.healink.integrador.domain.predicciones;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface PrediccionesMapper extends MapeadorGenerico<Predicciones, PrediccionesDTO> {

}
