package com.healink.integrador.domain.historia_clinica;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface HistoriaClinicaMapper extends MapeadorGenerico<HistoriaClinica, HistoriaClinicaDTO> {

}