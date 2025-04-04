package com.healink.integrador.domain.seguimientos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.atenciones_medicas.AtencionMedica;

import java.time.LocalDate;

@Entity
@Table(name = "SEGUIMIENTOS")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Seguimiento extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "atencion_id")
    private AtencionMedica atencion;

    @Column(name = "fecha_programada", nullable = false)
    private LocalDate fecha_programada;

    @Column(name = "fecha_realizada", nullable = false)
    private LocalDate fecha_realizada;
    
    @Column(name = "tipo", nullable = false)
    private String tipo;
    
    @Column(name = "resultado", nullable = false)
    private String resultado;

    @Column(name = "notas", nullable = false)
    private String notas;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "prioridad", nullable = false)
    private String prioridad;
}
