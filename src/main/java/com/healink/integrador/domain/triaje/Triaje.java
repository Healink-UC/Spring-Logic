package com.healink.integrador.domain.triaje;

import java.time.LocalDate;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.paciente.Paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "triajes")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Triaje extends EntidadAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Long pacienteId;

    @Column(name = "fecha_triaje", nullable = false)
    private LocalDate fechaTriaje;

    @Column(name = "edad", nullable = false)
    private int edad;

    @Column(name = "presion_sistolica", nullable = false)
    private float presionSistolica;

    @Column(name = "presion_diastolica", nullable = false)
    private float presionDiastolica;

    @Column(name = "colesterol_total", nullable = false)
    private float colesterolTotal;

    @Column(name = "hdl", nullable = false)
    private float hdl;

    @Column(name = "tabaquismo", nullable = false)
    private boolean tabaquismo;

    @Column(name = "alcoholismo", nullable = false)
    private boolean alcoholismo;

    @Column(name = "diabetes", nullable = false)
    private boolean diabetes;

    @Column(name = "peso", nullable = false)
    private float peso;

    @Column(name = "talla", nullable = false)
    private float talla;

    @Column(name = "imc", nullable = false)
    private float imc;

    @Column(name = "dolor_pecho", nullable = false)
    private boolean dolorPecho;

    @Column(name = "dolor_irradiado", nullable = false)
    private boolean dolorIrradiado;

    @Column(name = "sudoracion", nullable = false)
    private boolean sudoracion;

    @Column(name = "nauseas", nullable = false)
    private boolean nauseas;

    @Column(name = "antecedentes_cardiacos", nullable = false)
    private boolean antecedentesCardiacos;

    @Column(name = "resultado_riesgo_cardiovascular", nullable = false)
    private float resultadoRiesgoCardiovascular;

    @Column(name = "nivel_prioridad", nullable = false)
    @Enumerated(EnumType.STRING)
    private NivelPrioridad nivelPrioridad;

    // relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Paciente paciente;

}
