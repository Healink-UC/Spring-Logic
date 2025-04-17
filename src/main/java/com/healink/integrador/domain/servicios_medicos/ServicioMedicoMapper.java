package com.healink.integrador.domain.servicios_medicos;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicioMedicoMapper extends MapeadorGenerico<ServicioMedico, ServicioMedicoDTO> {
}
