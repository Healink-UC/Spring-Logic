// package com.healink.integrador.domain.embajadores_entidades;

// import com.healink.integrador.core.mapper.MapeadorGenerico;
// import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;

// @Mapper(componentModel = "spring")
// public interface EmbajadorEntidadMapper extends MapeadorGenerico<EmbajadorEntidad, EmbajadorEntidadDTO> {

//     @Override
//     @Mapping(target = "nombreEmbajador", expression = "java(entity.getEmbajador() != null ? entity.getEmbajador().getNombreCompleto() : null)")
//     @Mapping(target = "nombreEntidad", expression = "java(entity.getEntidadSalud() != null ? entity.getEntidadSalud().getRazonSocial() : null)")
//     EmbajadorEntidadDTO aDTO(EmbajadorEntidad entity);
// }