package com.healink.integrador.domain.inscripciones_campana;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.campana.Campana;
import com.healink.integrador.domain.paciente.Paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones_campana", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"paciente_id", "campana_id"})
})
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionCampana extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "campana_id", nullable = false)
    private Long campanaId;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoInscripcion estado = EstadoInscripcion.INSCRITO;

    @Column(name = "motivo_retiro")
    private String motivoRetiro;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campana_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Campana campana;
} 