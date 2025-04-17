package com.healink.integrador.domain.servicios_medicos;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServicioMedicoDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El nombre del servicio médico es requerido")
    private String nombre;

    @NotNull(message = "La descripción del servicio médico es requerida")
    private String descripcion;
}
