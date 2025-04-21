package com.healink.integrador.domain.atenciones_medicas;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface AtencionMedicaMapper extends MapeadorGenerico<AtencionMedica, AtencionMedicaDTO> {
}