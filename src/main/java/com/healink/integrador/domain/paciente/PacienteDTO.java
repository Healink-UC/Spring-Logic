package com.healink.integrador.domain.paciente;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    private LocalDate fechaNacimiento;

    @Pattern(regexp = "^[MFO]$", message = "Género debe ser M, F u O")
    private String genero;

    private String telefono;

    @Email(message = "Correo electrónico inválido")
    private String correo;

    private String direccion;

    private String localidad;

    @NotNull(message = "El ID de usuario es requerido")
    private Long usuarioId;

    private String estado;
}