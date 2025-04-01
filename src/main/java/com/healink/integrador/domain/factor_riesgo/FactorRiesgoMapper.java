package com.healink.integrador.domain.factor_riesgo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface FactorRiesgoMapper extends MapeadorGenerico<FactorRiesgo, FactorRiesgoDTO> {

    @Override
    // @Mapping(source = "id", target = "id")
    // @Mapping(source = "nombre", target = "nombre")
    // @Mapping(source = "descripcion", target = "descripcion")
    // @Mapping(source = "tipo", target = "tipo")
    FactorRiesgoDTO aDTO(FactorRiesgo dto);

    // @Mapping(source = "actualizadoPor", target = "actualizadoPor")
    // @Mapping(source = "creadoPor", target = "creadoPor")
    // @Mapping(source = "fechaActualizacion", target = "fechaActualizacion")
    // @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    void actualizarEntidadDesdeDTO(FactorRiesgoDTO dto, @MappingTarget FactorRiesgo entidad);
}
