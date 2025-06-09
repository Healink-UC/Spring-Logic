package com.healink.integrador.domain.usuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import com.healink.integrador.domain.entidades_salud.EntidadSaludMapper;

@Mapper(componentModel = "spring", uses = { EntidadSaludMapper.class })
public interface UsuarioMapper extends MapeadorGenerico<Usuario, UsuarioDTO> {

    @Override
    @Mapping(target = "clave", ignore = true)
    @Mapping(target = "rolId", source = "rolId")
    @Mapping(target = "entidadSalud", ignore = true) // Ignorar para evitar lazy loading
    UsuarioDTO aDTO(Usuario entity);

    @Override
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "entidadSalud", ignore = true)
    Usuario aEntidad(UsuarioDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "entidadSalud", ignore = true)
    void actualizarEntidadDesdeDTO(UsuarioDTO dto, @MappingTarget Usuario entity);

    // Método específico para cuando la entidad de salud está cargada
    @Named("conEntidadSalud")
    @Mapping(target = "clave", ignore = true)
    @Mapping(target = "rolId", source = "rolId")
    @Mapping(target = "entidadSalud", source = "entidadSalud")
    UsuarioDTO aDTOConEntidadSalud(Usuario entity);

}
