package com.healink.integrador.domain.campana_factor;

import org.mapstruct.Mapper;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface CampanaFactoresMapper extends MapeadorGenerico<CampanaFactores, CampanaFactoresDTO> {

}
