package com.healink.integrador.domain.embajadores_entidades;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import com.healink.integrador.domain.embajadores.EmbajadorMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EmbajadorMapper.class})
public interface EmbajadorEntidadMapper extends MapeadorGenerico<EmbajadorEntidad, EmbajadorEntidadDTO> {
    
    @Override
    @Mapping(target = "embajador", source = "embajador")
    EmbajadorEntidadDTO aDTO(EmbajadorEntidad entity);

    @AfterMapping
    default void mapearInformacionEmbajador(EmbajadorEntidad entity, @MappingTarget EmbajadorEntidadDTO dto) {
        if (entity.getEmbajador() != null) {
            
            if (entity.getEmbajador().getUsuario() != null) {
                dto.getEmbajador().setIdentificacion(entity.getEmbajador().getUsuario().getIdentificacion());
                dto.getEmbajador().setCorreo(entity.getEmbajador().getUsuario().getCorreo());
            }
        }
    }
} 