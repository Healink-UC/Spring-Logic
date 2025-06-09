package com.healink.integrador.domain.triaje;

import com.healink.integrador.core.dto.DTOBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TriajeDTO implements DTOBase {

    @Schema(readOnly = true, description = "Identificador único del triaje")
    private Long id;

    @NotNull(message = "El id del paciente es requerido")
    private Long pacienteId;

    @NotNull(message = "La edad es requerida")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    private int edad;

    @NotNull(message = "Debe registrar si el paciente realiza actividad física")
    private boolean actividadFisica;

    @NotNull(message = "El peso es requerido")
    @Positive(message = "El peso debe ser un valor positivo")
    @Max(value = 500, message = "El peso no puede ser mayor a 500 kg")
    private float peso;

    @NotNull(message = "La estatura es requerida")
    @Positive(message = "La estatura debe ser un valor positivo")
    @Max(value = 400, message = "La estatura no puede ser mayor a 400 cm (4 metros)")
    private float estatura;

    @NotNull(message = "Debe registrar si el paciente fuma")
    private boolean tabaquismo;

    @NotNull(message = "Debe registrar si el paciente bebe")
    private boolean alcoholismo;

    @NotNull(message = "Debe registrar si el paciente presenta diabetes")
    private boolean diabetes;

    @NotNull(message = "Debe registrar si el paciente tiene dolor de pecho")
    private boolean dolorPecho;

    @NotNull(message = "Debe registrar si el paciente tiene dolor irradiado")
    private boolean dolorIrradiado;

    @NotNull(message = "Debe registrar si el paciente presenta sudoración")
    private boolean sudoracion;

    @NotNull(message = "Debe registrar si el paciente presenta náuseas")
    private boolean nauseas;

    @NotNull(message = "Debe registrar si el paciente tiene antecedentes cardíacos")
    private boolean antecedentesCardiacos;

    @NotNull(message = "Debe registrar si el paciente padece hipertensión")
    private boolean hipertension;

    @NotNull(message = "La fecha del triaje es requerida")
    private LocalDate fechaTriaje;

    private String descripcion;
}
