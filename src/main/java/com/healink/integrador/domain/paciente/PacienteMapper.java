package com.healink.integrador.domain.paciente;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.GenericMapper;

@Mapper(componentModel = "spring")
public interface PacienteMapper extends GenericMapper<Paciente, PacienteDTO> {
}