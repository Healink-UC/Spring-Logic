package com.healink.integrador.domain.usuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import com.healink.integrador.domain.entidades_salud.EntidadSaludMapper;

@Mapper(componentModel = "spring", uses = { EntidadSaludMapper.class })
public abstract class UsuarioMapper implements MapeadorGenerico<Usuario, UsuarioDTO> {

    @Autowired
    protected UsuarioMapperHelper usuarioMapperHelper;

    @Override
    @Mapping(target = "clave", ignore = true)
    @Mapping(target = "rolId", source = "rolId")
    @Mapping(target = "creadoPorId", source = "creadoPor", qualifiedByName = "convertirCreadoPorAId")
    public abstract UsuarioDTO aDTO(Usuario entity);

    @Override
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    public abstract Usuario aEntidad(UsuarioDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    public abstract void actualizarEntidadDesdeDTO(UsuarioDTO dto, @MappingTarget Usuario entity);

    // Método específico para cuando la entidad de salud está cargada
    @Named("conEntidadSalud")
    @Mapping(target = "clave", ignore = true)
    @Mapping(target = "rolId", source = "rolId")
    @Mapping(target = "creadoPorId", source = "creadoPor", qualifiedByName = "convertirCreadoPorAId")
    public abstract UsuarioDTO aDTOConEntidadSalud(Usuario entity);

    @Named("convertirCreadoPorAId")
    protected Long convertirCreadoPorAId(String creadoPor) {
        return usuarioMapperHelper.convertirCreadoPorAId(creadoPor);
    }

}
