package com.healink.integrador.domain.campana;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface CampanaMapper extends MapeadorGenerico<Campana, CampanaDTO> {

}
