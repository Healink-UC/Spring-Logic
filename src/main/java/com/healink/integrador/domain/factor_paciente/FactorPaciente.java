package com.healink.integrador.domain.factor_paciente;

import java.time.LocalDate;

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
@Table(name = "factores_paciente")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FactorPaciente extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "factor_id", nullable = false)
    // private Factor factor; TODO: faltan las entidades (Factor riesgo)

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "triaje_id", nullable = false)
    // private Triaje triaje;TODO: faltan las entidades (Triaje)

    @Column(name = "valor", nullable = false, length = 350)
    private String valor;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

}
