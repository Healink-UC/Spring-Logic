package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import com.healink.integrador.domain.datos_clinicos.DatosClinicos;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CitacionMedicaMapper extends MapeadorGenerico<CitacionMedica, CitacionMedicaDTO> {
}
