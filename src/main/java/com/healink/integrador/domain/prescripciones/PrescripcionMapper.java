package com.healink.integrador.domain.prescripciones;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface PrescripcionMapper extends MapeadorGenerico<Prescripcion, PrescripcionDTO> {
        
    @Override
    @Mapping(target = "diagnostico_id", source = "diagnostico.id") // Extracción de Rol a rolId
    PrescripcionDTO aDTO(Prescripcion entity);

    @Override
    @Mapping(target = "diagnostico.id", source = "diagnostico_id")
    Prescripcion aEntidad(PrescripcionDTO dto);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "diagnostico.id", source = "diagnostico_id")
    void actualizarEntidadDesdeDTO(PrescripcionDTO dto, @MappingTarget Prescripcion entity);

}