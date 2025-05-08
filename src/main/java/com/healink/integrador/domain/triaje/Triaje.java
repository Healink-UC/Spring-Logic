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

    @Column(name = "edad", nullable = false)
    private int edad;

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

    @Column(name = "resultado_riesgo_cardiovascular", nullable = false)
    private float resultadoRiesgoCardiovascular;

    // relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Paciente paciente;

}
