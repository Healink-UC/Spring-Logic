package com.healink.integrador.domain.predicciones;

import org.mapstruct.Mapper;

import com.healink.integrador.core.json.JsonMapper;
import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring", uses = { JsonMapper.class })
public interface PrediccionMapper extends MapeadorGenerico<Prediccion, PrediccionDTO> {

}
