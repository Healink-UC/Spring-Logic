package com.healink.integrador.domain.recomendaciones;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface RecomendacionMapper extends MapeadorGenerico<Recomendacion, RecomendacionDTO> {
    @Override
    @Mapping(target = "diagnostico_id", source = "diagnostico.id") // Extracción de Rol a rolId
    RecomendacionDTO aDTO(Recomendacion entity);

    @Override
    @Mapping(target = "diagnostico.id", source = "diagnostico_id")
    Recomendacion aEntidad(RecomendacionDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "diagnostico.id", source = "diagnostico_id")
    void actualizarEntidadDesdeDTO(RecomendacionDTO dto, @MappingTarget Recomendacion entity);
}