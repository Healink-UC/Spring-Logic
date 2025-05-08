package com.healink.integrador.domain.personal_medico;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonalMedicoDTO implements DTOBase {

    private Long id;

    private String especialidad;

    @NotNull(message = "El ID de entidad es requerido")
    private Long entidadId;

    @NotNull(message = "El ID de usuario es requerido")
    private Long usuarioId;
}