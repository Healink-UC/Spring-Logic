package com.healink.integrador.domain.recomendaciones;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RecomendacionDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador del diagnostico es requerido")
    private Long diagnostico_id;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotBlank(message = "El nivel de importancia es requerido")
    @Pattern(regexp = "^(ALTA|MEDIA|BAJA)$", message = "Tipo de recomendación inválido. Debe ser 'alta', 'media' o 'baja'.")
    private String nivel_importancia;

    @NotBlank(message = "El tipo de recomendación es requerido")
    @Pattern(regexp = "^(MEDICAMENTO|ESTILO_VIDA|PREVENCION)$", message = "Tipo de recomendación inválido. Debe ser 'medicamento', 'estilo_vida' o 'prevencion'.")
    private String tipo;

    @NotNull(message = "La fecha de creacion es requerida")
    private LocalDate fecha_creacion;

}