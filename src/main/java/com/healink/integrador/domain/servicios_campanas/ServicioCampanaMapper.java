package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.mapper.MapeadorGenerico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicioCampanaMapper extends MapeadorGenerico<ServicioCampana, ServicioCampanaDTO> {
}
