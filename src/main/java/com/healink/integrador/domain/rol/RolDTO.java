package com.healink.integrador.domain.rol;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RolDTO implements DTOBase {

    private Long id;

    @NotBlank(message = "El nombre del rol es requerido")
    @Pattern(regexp = "^(administrador|desarrollador|entidad_salud|medico|auxiliar|paciente|embajador)$", message = "Nombre de rol inválido")
    private String nombre;

    private String descripcion;
}
