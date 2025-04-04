package com.healink.integrador.domain.diagnosticos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.atenciones_medicas.AtencionMedica;

import java.time.LocalDate;

@Entity
@Table(name = "DIAGNOSTICOS")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Diagnostico extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "atencion_id")
    private AtencionMedica atencion;    
    
    @Column(name = "codigo_cie10", nullable = false)
    private String codigoCie10;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "es_principal", nullable = false)
    private boolean es_principal;

    @Column(name = "severidad", nullable = false)
    private String severidad;

    @Column(name = "fecha_diagnostico", nullable = false)
    private LocalDate fecha_diagnostico;
}
