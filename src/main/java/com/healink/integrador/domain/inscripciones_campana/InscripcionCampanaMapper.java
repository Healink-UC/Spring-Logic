package com.healink.integrador.domain.inscripciones_campana;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface InscripcionCampanaMapper extends MapeadorGenerico<InscripcionCampana, InscripcionCampanaDTO> {
} 