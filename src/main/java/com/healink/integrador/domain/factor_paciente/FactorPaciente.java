package com.healink.integrador.domain.factor_paciente;

import java.time.LocalDate;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.factor_riesgo.FactorRiesgo;
import com.healink.integrador.domain.paciente.Paciente;
import com.healink.integrador.domain.triaje.Triaje;

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
@Table(name = "factores_paciente")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FactorPaciente extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "factor_id", nullable = false)
    private Long factorId;

    @Column(name = "observacion", nullable = false, length = 350)
    private String observacion;

    // relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, insertable = false, updatable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factor_id", nullable = false, insertable = false, updatable = false)
    private FactorRiesgo factor;

}
