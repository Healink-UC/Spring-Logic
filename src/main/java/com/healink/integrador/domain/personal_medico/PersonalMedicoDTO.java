package com.healink.integrador.domain.personal_medico;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PersonalMedicoDTO implements DTOBase {

    private Long id;

    @NotBlank(message = "El nombre completo es requerido")
    private String nombreCompleto;

    private String especialidad;

    private String telefono;

    @NotNull(message = "El ID de entidad es requerido")
    private Long entidadId;

    @NotNull(message = "El ID de usuario es requerido")
    private Long usuarioId;

    @Pattern(regexp = "^(ACTIVO|INACTIVO)$", message = "Estado debe ser ACTIVO o INACTIVO")
    private String estado;
}