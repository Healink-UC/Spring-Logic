package com.healink.integrador.domain.diagnosticos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface DiagnosticoMapper extends MapeadorGenerico<Diagnostico, DiagnosticoDTO> {

    @Override
    @Mapping(target = "citacionId", source = "citacion.id") // Extracción de Citacion a citacionId
    DiagnosticoDTO aDTO(Diagnostico entity);

    @Override
    @Mapping(target = "citacion.id", source = "citacionId")
    Diagnostico aEntidad(DiagnosticoDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "citacion.id", source = "citacionId")
    void actualizarEntidadDesdeDTO(DiagnosticoDTO dto, @MappingTarget Diagnostico entity);

}