package com.healink.integrador.domain.atenciones_medicas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "ATENCIONES_MEDICAS")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class AtencionMedica extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citacion_id", nullable = false)
    private Long citacionId;
    
    @Column(name = "fecha_hora_inicio", nullable = false)
    private Timestamp fechaHoraInicio;

    @Column(name = "fecha_hora_fin", nullable = false)
    private Timestamp fechaHoraFin;

    @Column(name = "duracion_real", nullable = false)
    private int duracionReal;

    @Column(name = "notas_medicas", nullable = false)
    private String notasMedicas;

    @Column(name = "notas_estructuradas")
    private String notasEstructuradas;

    @Column(name = "estado", nullable = false)
    private String estado;
}
