package com.healink.integrador.domain.usuario;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UsuarioDTO implements DTOBase {

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

    private String clave;

    private String celular;
    private Boolean estaActivo;
    private Long rolId;
}