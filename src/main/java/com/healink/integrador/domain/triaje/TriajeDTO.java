package com.healink.integrador.domain.triaje;

import java.time.LocalDate;

import com.healink.integrador.core.dto.DTOBase;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TriajeDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El id del paciente es requerido")
    private Long pacienteId;

    @NotNull(message = "La fecha del triaje es requerido")
    private LocalDate fechaTriaje;

    @NotNull(message = "La edad es requerida")
    private int edad;

    @NotNull(message = "La presión sistólica es requerida")
    private float presionSistolica;

    @NotNull(message = "La presion diastólica es requerida")
    private float presionDiastolica;

    @NotNull(message = "El colesterol total es requerido")
    private float colesterolTotal;

    @NotNull(message = "El hdl es requerido")
    private float hdl;

    @NotNull(message = "Debe registrar si el paciente fuma")
    private boolean tabaquismo;

    @NotNull(message = "Debe registrar si el paciente bebe")
    private boolean alcoholismo;

    @NotNull(message = "Debe registrar si el paciente presenta diabetes")
    private boolean diabetes;

    @NotNull(message = "El peso del paciente es requerido")
    private float peso;

    @NotNull(message = "La talla del paciente es requerida")
    private float talla;

    @NotNull(message = "El IMC del paciente es requerido")
    private float imc;

    @NotNull(message = "Debe registrar si el paciente tiene dolo de pecho")
    private boolean dolorPecho;

    @NotNull(message = "Debe registrar si el paciente tiene dolor irradiado")
    private boolean dolorIrradiado;

    @NotNull(message = "Debe registrar si el paciente presenta sudoracion")
    private boolean sudoracion;

    @NotNull(message = "Debe registrar si el paciente presenta nauseas")
    private boolean nauseas;

    @NotNull(message = "Debe registrar si el paciente tiene antecedenres cardiacos")
    private boolean antecedentesCardiacos;

    @NotNull(message = "Debe registrar el riesgo cardiovascular del paciente")
    private float resultadoRiesgoCardiovascular;

    @NotNull(message = "El triaje debe tener un nivel de prioridad válido")
    private NivelPrioridad nivelPrioridad;

}
