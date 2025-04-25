package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServicioCampanaDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El el campo id servicio médico es requerido")
    private String servicioId;

    @NotNull(message = "El el campo id campaña es requerido")
    private String campanaId;
}
