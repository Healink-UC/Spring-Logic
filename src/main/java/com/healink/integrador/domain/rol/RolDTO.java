package com.healink.integrador.domain.rol;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RolDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotBlank(message = "El nombre del rol es requerido")
    private String nombre;

    @NotBlank(message = "Poner alguna cosita")
    private String descripcion;

    @NotBlank(message = "Permisos dentro del aplicativo")
    private String permisos;

}
