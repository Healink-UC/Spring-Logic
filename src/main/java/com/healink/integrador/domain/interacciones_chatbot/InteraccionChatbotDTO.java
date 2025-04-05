package com.healink.integrador.domain.interacciones_chatbot;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InteraccionChatbotDTO implements DTOBase {

    @Schema(readOnly = true, description = "Indica es sólo lectura")
    private Long id;

    @NotNull(message = "El identificador del seguimiento es requerido")
    private Long seguimiento_id;

    @NotNull(message = "El identificador del paciente es requerido")
    private Long paciente_id;

    @NotNull(message = "La fecha y hora de la interacción son requeridas")
    private Timestamp fecha_hora;

    @NotBlank(message = "La entrada de la interacción es requerida")
    private String entrada;

    @NotBlank(message = "La respuesta de la interacción es requerida")
    private String respuesta;

    private String intent_detectado;

    private String entidades_detectadas;

    private String contexto_conversacion;
}