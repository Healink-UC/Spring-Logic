package com.healink.integrador.domain.predicciones;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.healink.integrador.core.json.JsonMapper;
import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring", uses = { JsonMapper.class })
public interface PrediccionMapper extends MapeadorGenerico<Prediccion, PrediccionDTO> {

    @Mapping(target = "factoresInfluyentes", source = "factoresInfluyentes")
    @Mapping(target = "recomendaciones", source = "recomendaciones")
    PrediccionDTO aDTO(Prediccion entidad);

    @Mapping(target = "factoresInfluyentes", source = "factoresInfluyentes")
    @Mapping(target = "recomendaciones", source = "recomendaciones")
    Prediccion aEntidad(PrediccionDTO dto);
}
