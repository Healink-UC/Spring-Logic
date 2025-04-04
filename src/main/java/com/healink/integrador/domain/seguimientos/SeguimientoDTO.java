package com.healink.integrador.domain.seguimientos;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SeguimientoDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador de la atención médica es requerido")
    private Long atencion_id;

    @NotNull(message = "La fecha de programación es requerida")
    private LocalDate fecha_programada;
    
    private LocalDate fecha_realizada;
    
    @NotBlank(message = "El tipo de seguimiento es requerido")
    @Pattern(regexp = "^(LLAMADA|SMS|PRESENCIAL)$", message = "Tipo de seguimiento inválido. Debe ser 'llamada', 'sms' o 'presencial'.")
    private String tipo;

    @NotBlank(message = "El resultado es requerido")
    private String resultado;

    private String notas;

    @NotBlank(message = "El estado es requerido")
    @Pattern(regexp = "^(REALIZADO|PENDIENTE|CANCELADO)$", message = "Estado de seguimiento inválido. Debe ser 'realizado', 'pendiente' o 'cancelado'.")
    private String estado;

    @NotBlank(message = "La prioridad es requerido")
    @Pattern(regexp = "^(ALTA|MEDIA|BAJA)$", message = "Prioridad inválida. Debe ser 'alta', 'media' o 'baja'.")
    private String prioridad;


}