package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmbajadorMapper extends MapeadorGenerico<Embajador, EmbajadorDTO> {
}
