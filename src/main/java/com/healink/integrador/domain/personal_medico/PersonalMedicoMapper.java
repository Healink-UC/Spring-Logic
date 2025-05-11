package com.healink.integrador.domain.personal_medico;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface PersonalMedicoMapper extends MapeadorGenerico<PersonalMedico, PersonalMedicoDTO> {

}
