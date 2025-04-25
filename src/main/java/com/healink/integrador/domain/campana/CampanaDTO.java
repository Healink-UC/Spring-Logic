package com.healink.integrador.domain.campana;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

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

    @NotBlank(message = "La campaña debe tener una localizacion")
    private Long localizacionId;

    @NotBlank(message = "La campaña debe tener una fecha límite")
    private LocalDate fechaLimiteInscripcion;

    @NotBlank(message = "La campaña debe tener una fecha de inicio")
    private LocalDate fechaInicio;

    @NotBlank(message = "La campaña debe tener una fecha de finalizacion")
    private LocalDate fechaLimite;

    @NotBlank(message = "La campaña debe tener un número mínimo de participantes")
    private int minParticipantes;

    @NotBlank(message = "La campaña debe tener un número máximo de participantes")
    private int maxParticipantes;

    @NotBlank(message = "La campaña debe estar asociada a una entidad")
    private Long entidadId;

    @NotNull(message = "La campaña debe tener un estado válido")
    private EstadoCampana estado;
}
