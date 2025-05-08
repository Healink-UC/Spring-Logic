package com.healink.integrador.domain.datos_clinicos;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface DatosClinicosMapper extends MapeadorGenerico<DatosClinicos, DatosClinicosDTO> {

}
