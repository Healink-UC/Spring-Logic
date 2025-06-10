package com.healink.integrador.domain.rol;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RolDTO implements DTOBase {

    @Schema(readOnly = true, description = "Identificador único del rol")
    private Long id;

    @NotBlank(message = "El nombre del rol es requerido")
    @Size(max = 50, message = "El nombre del rol no puede exceder 50 caracteres")
    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String nombre;

    @NotBlank(message = "La descripción del rol es requerida")
    @Schema(description = "Descripción del rol", example = "Administrador del sistema")
    private String descripcion;

    @Schema(description = "Permisos del rol en formato JSON", example = "{\"usuarios\": [\"crear\", \"leer\", \"actualizar\", \"eliminar\"], \"campanas\": [\"leer\"]}")
    private JsonNode permisos;
}
