package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServicioCampanaDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El nombre de la campaña es requerido")
    private String nombre;

    @NotNull(message = "La descripción de la campaña es requerida")
    private String descripcion;
}
