package com.healink.integrador.domain.campana_factor;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CampanaFactoresDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El id de la campaña es requerido")
    private Long campanaId;

    @NotNull(message = "El id de factor de riesgo es requerido")
    private Long factorId;

}
