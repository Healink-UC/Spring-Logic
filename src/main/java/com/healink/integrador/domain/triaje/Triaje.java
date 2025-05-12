package com.healink.integrador.domain.triaje;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.paciente.Paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.LocalDate;

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

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "edad", nullable = false)
    private int edad;

    @Column(name = "actividad_fisica", nullable = false)
    private boolean actividadFisica;

    @Column(name = "peso", nullable = false)
    private float peso;

    @Column(name = "estatura", nullable = false)
    private float estatura;

    @Column(name = "tabaquismo", nullable = false)
    private boolean tabaquismo;

    @Column(name = "alcoholismo", nullable = false)
    private boolean alcoholismo;

    @Column(name = "diabetes", nullable = false)
    private boolean diabetes;

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

    @Column(name = "hipertension", nullable = false)
    private boolean hipertension;

    // @Column(name = "resultado_riesgo_cardiovascular", nullable = false)
    // private float resultadoRiesgoCardiovascular;

    // @Column(name = "resultado_riesgo_cv", nullable = false)
    // private float resultadoRiesgoCv;

    @Column(name = "fecha_triaje", nullable = false)
    private LocalDate fechaTriaje;

    // @Column(name = "nivel_prioridad", nullable = false)
    // @Enumerated(EnumType.STRING)
    // private NivelPrioridad nivelPrioridad;

    @Column(name = "descripcion")
    private String descripcion;

    // relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Paciente paciente;
}
