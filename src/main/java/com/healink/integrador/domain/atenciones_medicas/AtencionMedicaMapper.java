package com.healink.integrador.domain.atenciones_medicas;

import org.mapstruct.Mapper;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring", uses = { JsonMapper.class })
public interface AtencionMedicaMapper extends MapeadorGenerico<AtencionMedica, AtencionMedicaDTO> {
}