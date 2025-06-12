package com.healink.integrador.domain.personal_medico;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonalMedicoMapper extends MapeadorGenerico<PersonalMedico, PersonalMedicoDTO> {

    @Mapping(target = "entidadNombre", expression = "java(personalMedico.getEntidadSalud() != null ? personalMedico.getEntidadSalud().getRazonSocial() : null)")
    @Mapping(target = "usuarioNombre", expression = "java(personalMedico.getUsuario() != null ? personalMedico.getUsuario().getNombres() : null)")
    @Mapping(target = "creadoPorId", ignore = true)
    @Mapping(target = "actualizadoPorId", ignore = true)
    PersonalMedicoDTO aDTO(PersonalMedico personalMedico);

    @Mapping(target = "entidadSalud", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "actualizadoPor", ignore = true)
    PersonalMedico aEntidad(PersonalMedicoDTO dto);
} 