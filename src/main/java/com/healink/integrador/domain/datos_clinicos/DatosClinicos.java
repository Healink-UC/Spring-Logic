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

    @Column(name = "presion_sistolica", nullable = false)
    private Double presionSistolica;

    @Column(name = "presion_diastolica", nullable = false)
    private Double presionDiastolica;

    @Column(name = "frecuencia_cardiaca_min")
    private Double frecuenciaCardiacaMin;

    @Column(name = "frecuencia_cardiaca_max")
    private Double frecuenciaCardiacaMax;

    @Column(name = "saturacion_oxigeno")
    private Double saturacionOxigeno;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "colesterol_total", nullable = false)
    private Double colesterolTotal;

    @Column(name = "hdl", nullable = false)
    private Double hdl;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_medicion", nullable = false)
    private LocalDate fechaMedicion;
}