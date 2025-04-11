package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.mapper.MapeadorGenerico;
// import com.healink.integrador.domain.entidades_salud.EntidadSalud;
// import com.healink.integrador.domain.entidades_salud.EntidadSaludDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmbajadorMapper extends MapeadorGenerico<Embajador, EmbajadorDTO> {
}
