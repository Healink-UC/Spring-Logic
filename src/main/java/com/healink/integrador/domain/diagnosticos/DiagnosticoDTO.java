package com.healink.integrador.domain.diagnosticos;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DiagnosticoDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador de la atención es requerido")
    private Long atencionId;

    @NotBlank(message = "El codigo de la CIE10 es requerido")
    private String codigoCie10;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotNull(message = "Es requerido indicar si es el diagnóstico principal")
    private boolean es_principal;

    @NotBlank(message = "Es requerido indicar la severidad del diagnóstico")
    @Pattern(regexp = "^(LEVE|MODERADA|GRAVE)$", message = "Tipo de severidad inválida. Debe ser 'leve', 'moderada' o 'grave'.")
    private String severidad;

    @NotNull(message = "La fecha de diagnostico es requerida")
    private LocalDate fecha_diagnostico;

}