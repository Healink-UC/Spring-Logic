package com.healink.integrador.domain.campana;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;
import com.healink.integrador.core.validator.FechasValidator;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CampanaDTO implements DTOBase {

    private Long id;

    @NotBlank(message = "La campaña debe tener un nombre")
    private String nombre;

    @NotBlank(message = "La campaña debe tener una descripcion")
    private String descripcion;

    @NotNull(message = "La campaña debe tener una localizacion")
    private Long localizacionId;

    @NotNull(message = "La campaña debe tener una fecha límite")
    @FechasValidator(compareWith = "fechaInicio", message = "La fecha límite de inscripción debe ser anterior a la fecha de inicio de la campaña.")
    private LocalDate fechaLimiteInscripcion;

    @NotNull(message = "La campaña debe tener una fecha de inicio")
    @FechasValidator(compareWith = "fechaLimite", message = "La fecha de inicio debe ser anterior a la fecha de finalizacion de la campaña.")
    private LocalDate fechaInicio;

    @NotNull(message = "La campaña debe tener una fecha de finalizacion")
    @FechasValidator(compareWith = "fechaInicio", message = "La fecha de finalizacion debe ser posterior a la fecha de inicio de la campaña.")
    private LocalDate fechaLimite;

    @NotNull(message = "La campaña debe tener un número mínimo de participantes")
    @Min(value = 10, message = "El número mínimo de participantes de la campaña es 10")
    private int minParticipantes;

    @NotNull(message = "La campaña debe tener un número máximo de participantes")
    @Max(value = 200, message = "El número máximo de participantes de la campaña es 200.")
    private int maxParticipantes;

    @NotNull(message = "La campaña debe estar asociada a una entidad")
    private Long entidadId;

    @NotNull(message = "La campaña debe tener un estado válido")
    private EstadoCampana estado;
}
