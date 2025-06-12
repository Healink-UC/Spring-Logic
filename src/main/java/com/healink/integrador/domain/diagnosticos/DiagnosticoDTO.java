package com.healink.integrador.domain.diagnosticos;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DiagnosticoDTO implements DTOBase {

    @Schema(readOnly = true, description = "ID del diagnóstico")
    private Long id;

    @NotNull(message = "El identificador de la citación médica es requerido")
    private Long citacionId;

    @Schema(description = "Código CIE-10 del diagnóstico")
    private String codigoCie10;

    @Schema(description = "Descripción del diagnóstico")
    private String descripcion;

    @Schema(description = "Indica si es el diagnóstico principal")
    private Boolean es_principal;

    @Schema(description = "Severidad del diagnóstico")
    private String severidad;

    @Schema(description = "Fecha del diagnóstico")
    private LocalDate fecha_diagnostico;

}