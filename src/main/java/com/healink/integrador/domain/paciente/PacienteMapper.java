package com.healink.integrador.domain.paciente;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface PacienteMapper extends MapeadorGenerico<Paciente, PacienteDTO> {
}