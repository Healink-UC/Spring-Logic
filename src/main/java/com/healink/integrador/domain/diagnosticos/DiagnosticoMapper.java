package com.healink.integrador.domain.diagnosticos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface DiagnosticoMapper extends MapeadorGenerico<Diagnostico, DiagnosticoDTO> {

    @Override
    @Mapping(target = "citacionId", source = "citacion.id")
    DiagnosticoDTO aDTO(Diagnostico entity);

}