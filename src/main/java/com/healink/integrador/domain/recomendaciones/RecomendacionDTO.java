package com.healink.integrador.domain.recomendaciones;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecomendacionDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador del diagnostico es requerido")
    private Long diagnostico_id;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotNull(message = "El nivel de importancia es requerido")
    private NivelPrioridad nivel_importancia;

    @NotNull(message = "El tipo de recomendación es requerido")
    private TipoRecomendaciones tipo;

    @NotNull(message = "La fecha de creacion es requerida")
    private LocalDate fecha_creacion;

}