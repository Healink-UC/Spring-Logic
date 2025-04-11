package com.healink.integrador.domain.localizacion;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocalizacionMapper extends MapeadorGenerico<Localizacion, LocalizacionDTO> {
}
