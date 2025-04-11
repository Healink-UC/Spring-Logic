package com.healink.integrador.domain.predicciones;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrediccionesDTO implements DTOBase {

    private Long id;

    @NotBlank(message = "El id del paciente es requerido")
    private Long pacienteId;

    @NotBlank(message = "El id de la campaña es requerido")
    private Long campanaId;

    @NotBlank(message = "El valor de predicción es requerido")
    private float valorPrediccion;

    @NotBlank(message = "El nivel de confianza es requerido")
    private float confianza;

    @NotNull(message = "Factores influyentes es requerido")
    private JsonNode factoresInfluyentes;

    @NotBlank(message = "La fecha de prediccion es requerida")
    private LocalDate fechaPrediccion;

    @NotBlank(message = "La version del modelo es requerida")
    private String modeloVersion;

    @NotNull(message = "El tipo de prediccion debe tener un valor válido")
    private TipoPrediccion tipo;

}
