package com.healink.integrador.domain.historia_clinica;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface HistoriaClinicaMapper extends MapeadorGenerico<HistoriaClinica, HistoriaClinicaDTO> {

    @Override
    @Mapping(target = "fechaCreacion", ignore = true)
    void actualizarEntidadDesdeDTO(HistoriaClinicaDTO dto, @MappingTarget HistoriaClinica entidad);
}