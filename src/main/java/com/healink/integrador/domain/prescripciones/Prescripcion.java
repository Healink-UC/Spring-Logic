package com.healink.integrador.domain.prescripciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.diagnosticos.Diagnostico;

import java.time.LocalDate;

@Entity
@Table(name = "PRESCRIPCIONES")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Prescripcion extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diagnostico_id")
    private Diagnostico diagnostico;
    
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "dosis", nullable = false)
    private String dosis;

    @Column(name = "frecuencia", nullable = false)
    private String frecuencia;

    @Column(name = "duracion", nullable = false)
    private String duracion;

    @Column(name = "indicaciones_especiales")
    private String indicaciones_especiales;

    @Column(name = "fecha_prescripcion", nullable = false)
    private LocalDate fecha_prescripcion;
}
