package com.healink.integrador.domain.historia_clinica;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "HISTORIAS_CLINICAS")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "triaje_id")
    private Long triajeId;

    @Column(name = "datos_clinicos_id")
    private Long datosClinicosId;

    @Column(name = "citacion_id")
    private Long citacionId;

    @Column(name = "prob_rehospitalizacion")
    private Double probRehospitalizacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}