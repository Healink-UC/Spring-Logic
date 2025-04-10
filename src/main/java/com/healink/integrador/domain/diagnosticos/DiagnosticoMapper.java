package com.healink.integrador.domain.diagnosticos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface DiagnosticoMapper extends MapeadorGenerico<Diagnostico, DiagnosticoDTO> {

    @Override
    @Mapping(target = "atencionId", source = "atencion.id") // Extracción de Rol a rolId
    DiagnosticoDTO aDTO(Diagnostico entity);

    @Override
    @Mapping(target = "atencion.id", source = "atencionId")
    Diagnostico aEntidad(DiagnosticoDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "atencion.id", source = "atencionId")
    void actualizarEntidadDesdeDTO(DiagnosticoDTO dto, @MappingTarget Diagnostico entity);

}