package com.healink.integrador.domain.seguimientos;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeguimientoDTO implements DTOBase {

    @Schema(readOnly = true, description = "ID del seguimiento")
    private Long id;

    @NotNull(message = "El identificador de la citación médica es requerido")
    private Long citacion_id;

    @Schema(description = "Fecha programada para el seguimiento")
    private LocalDate fecha_programada;

    @Schema(description = "Fecha en que se realizó el seguimiento")
    private LocalDate fecha_realizada;

    @Schema(description = "Tipo de seguimiento")
    private String tipo;

    @Schema(description = "Resultado del seguimiento")
    private String resultado;

    @Schema(description = "Notas adicionales")
    private String notas;

    @Schema(description = "Estado del seguimiento")
    private String estado;

    @Schema(description = "Nivel de prioridad")
    private String prioridad;

}