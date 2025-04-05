package com.healink.integrador.domain.recomendaciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.diagnosticos.Diagnostico;

@Entity
@Table(name = "RECOMENDACIONES")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Recomendacion extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diagnostico_id")
    private Diagnostico diagnostico;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "nivel_importancia", nullable = false)
    private String nivel_importancia;

    @Column(name = "tipo", nullable = false)
    private String tipo;
}
