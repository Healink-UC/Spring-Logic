package com.healink.integrador.domain.triaje;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TriajeDTO implements DTOBase {

    private Long id;

    @NotBlank(message = "El id del paciente es requerido")
    private Long pacienteId;

    @NotBlank(message = "La fecha del triaje es requerido")
    private LocalDate fechaTriaje;

    @NotBlank(message = "La edad es requerida")
    private int edad;

    @NotBlank(message = "La presión sistólica es requerida")
    private float presionSistolica;

    @NotBlank(message = "La presion diastólica es requerida")
    private float presionDiastolica;

    @NotBlank(message = "El colesterol total es requerido")
    private float colesterolTotal;

    @NotBlank(message = "El hdl es requerido")
    private float hdl;

    @NotBlank(message = "Debe registrar si el paciente fuma")
    private boolean tabaquismo;

    @NotBlank(message = "Debe registrar si el paciente bebe")
    private boolean alcoholismo;

    @NotBlank(message = "Debe registrar si el paciente presenta diabetes")
    private boolean diabetes;

    @NotBlank(message = "El peso del paciente es requerido")
    private float peso;

    @NotBlank(message = "La talla del paciente es requerida")
    private float talla;

    @NotBlank(message = "El IMC del paciente es requerido")
    private float imc;

    @NotBlank(message = "Debe registrar si el paciente tiene dolo de pecho")
    private boolean dolorPecho;

    @NotBlank(message = "Debe registrar si el paciente tiene dolor irradiado")
    private boolean dolorIrradiado;

    @NotBlank(message = "Debe registrar si el paciente presenta sudoracion")
    private boolean sudoracion;

    @NotBlank(message = "Debe registrar si el paciente presenta nauseas")
    private boolean nauseas;

    @NotBlank(message = "Debe registrar si el paciente tiene antecedenres cardiacos")
    private boolean antecedentesCardiacos;

    @NotBlank(message = "Debe registrar el riesgo cardiovascular del paciente")
    private float resultadoRiesgoCardiovascular;

    @NotNull(message = "El triaje debe tener un nivel de prioridad válido")
    private NivelPrioridad nivelPrioridad;

}
