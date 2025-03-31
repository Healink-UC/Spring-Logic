package com.healink.integrador.domain.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UsuarioDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotBlank(message = "El tipo de identificación es requerido")
    @Pattern(regexp = "^(cc|ti|nit|rcn)$", message = "Tipo de identificación inválido")
    private String tipoIdentificacion;

    @NotBlank(message = "La identificación es requerida")
    private String identificacion;

    @NotBlank(message = "El nombre es requerido")
    private String nombres;

    @NotBlank(message = "Los apellidos son requeridos")
    private String apellidos;

    @Email(message = "Correo electrónico inválido")
    private String correo;

    // Solo permitir setear la clave en solicitudes, nunca devolverla en respuestas
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clave;

    private String celular;
    private Boolean estaActivo = true; // Valor predeterminado

    // @NotNull(message = "El rol es requerido")
    private Long rolId;

}