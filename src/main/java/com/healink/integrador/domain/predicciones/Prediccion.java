package com.healink.integrador.domain.predicciones;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.campana.Campana;
import com.healink.integrador.domain.paciente.Paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "predicciones")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Prediccion extends EntidadAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false, insertable = false, updatable = false)
    private Long pacienteId;

    @Column(name = "campana_id", nullable = false, insertable = false, updatable = false)
    private Long campanaId;

    @Column(name = "valor_prediccion", nullable = false)
    private float valorPrediccion;

    @Column(name = "confianza", nullable = false)
    private float confianza;

    @Column(name = "factores_influyentes", nullable = false, columnDefinition = "jsonb")
    private JsonNode factoresInfluyentes;

    @Column(name = "fecha_prediccion", nullable = false)
    private LocalDate fechaPrediccion;

    @Column(name = "modeloVersion", nullable = false)
    private String modeloVersion;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPrediccion tipo;

    // relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campana_id", nullable = false)
    private Campana campana;
}
