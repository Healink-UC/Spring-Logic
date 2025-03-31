package com.healink.integrador.domain.datos_clinicos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface DatosClinicosMapper extends MapeadorGenerico<DatosClinicos, DatosClinicosDTO> {

    @Override
    @Mapping(target = "fechaRegistro", ignore = true)
    void actualizarEntidadDesdeDTO(DatosClinicosDTO dto, @MappingTarget DatosClinicos entidad);
}
