package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntidadSaludMapper extends MapeadorGenerico<EntidadSalud, EntidadSaludDTO> {
}
