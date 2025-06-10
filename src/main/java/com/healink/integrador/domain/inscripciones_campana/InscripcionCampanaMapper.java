package com.healink.integrador.domain.inscripciones_campana;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface InscripcionCampanaMapper extends MapeadorGenerico<InscripcionCampana, InscripcionCampanaDTO> {

    @Override
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "campana", ignore = true)
    InscripcionCampana aEntidad(InscripcionCampanaDTO dto);
}