package com.healink.integrador.domain.datos_clinicos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "DATOS_CLINICOS")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class DatosClinicos extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro = LocalDate.now();

    @Column(name = "presion_sistolica")
    private Double presionSistolica;

    @Column(name = "presion_diastolica")
    private Double presionDiastolica;

    @Column(name = "frecuencia_cardiaca_min")
    private Double frecuenciaCardiacaMin;

    @Column(name = "frecuencia_cardiaca_max")
    private Double frecuenciaCardiacaMax;

    @Column(name = "saturacion_oxigeno")
    private Double saturacionOxigeno;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "talla")
    private Double talla;

    @Column(name = "imc")
    private Double imc;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}