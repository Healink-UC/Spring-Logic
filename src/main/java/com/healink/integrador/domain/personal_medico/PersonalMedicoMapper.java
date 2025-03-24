package com.healink.integrador.domain.personal_medico;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface PersonalMedicoMapper extends MapeadorGenerico<PersonalMedico, PersonalMedicoDTO> {

    @Override
    @Mapping(target = "fechaRegistro", ignore = true)
    void actualizarEntidadDesdeDto(PersonalMedicoDTO dto, @MappingTarget PersonalMedico entidad);
}
