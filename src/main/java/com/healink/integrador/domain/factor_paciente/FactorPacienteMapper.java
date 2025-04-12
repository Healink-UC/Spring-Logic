package com.healink.integrador.domain.factor_paciente;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface FactorPacienteMapper extends MapeadorGenerico<FactorPaciente, FactorPacienteDTO> {

}
