package com.healink.integrador.domain.historia_clinica;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.datos_clinicos.DatosClinicos;
import com.healink.integrador.domain.diagnosticos.Diagnostico;
import com.healink.integrador.domain.prescripciones.Prescripcion;
import com.healink.integrador.domain.recomendaciones.Recomendacion;
import com.healink.integrador.domain.seguimientos.Seguimiento;
import com.healink.integrador.domain.triaje.Triaje;

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
    
    @Column(name = "paciente_id", nullable = false, unique = true)
    private Long pacienteId;

    @OneToOne
    @JoinColumn(name = "triaje_id", referencedColumnName = "id")
    private Triaje ultimoTriaje;

    @OneToOne
    @JoinColumn(name = "datos_clinicos_id", referencedColumnName = "id")
    private DatosClinicos ultimosDatosClinicos;

    @OneToOne
    @JoinColumn(name = "diagnostico_id", referencedColumnName = "id")
    private Diagnostico ultimoDiagnostico;

    @OneToOne
    @JoinColumn(name = "recomendacion_id", referencedColumnName = "id")
    private Recomendacion ultimaRecomendacion;

    @OneToOne
    @JoinColumn(name = "seguimiento_id", referencedColumnName = "id")
    private Seguimiento ultimoSeguimiento;

    @OneToOne
    @JoinColumn(name = "prescripcion_id", referencedColumnName = "id")
    private Prescripcion ultimaPrescripcion;

    @Column(name = "prob_rehospitalizacion")
    private Double probRehospitalizacion;
}