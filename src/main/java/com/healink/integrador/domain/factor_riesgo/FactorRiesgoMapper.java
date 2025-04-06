package com.healink.integrador.domain.factor_riesgo;

import org.mapstruct.Mapper;
import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface FactorRiesgoMapper extends MapeadorGenerico<FactorRiesgo, FactorRiesgoDTO> {
}
