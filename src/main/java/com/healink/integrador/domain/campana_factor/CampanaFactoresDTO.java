package com.healink.integrador.domain.campana_factor;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CampanaFactoresDTO implements DTOBase {

    private Long id;

    @NotBlank(message = "El id de la campaña es requerido")
    private Long campana_id;

    @NotBlank(message = "El id de factor de riesgo es requerido")
    private Long factor_id;

}
