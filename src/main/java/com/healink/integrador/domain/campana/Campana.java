package com.healink.integrador.domain.campana;

import java.time.LocalDate;

import com.healink.integrador.core.entity.EntidadAuditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campanas")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Campana extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 350)
    private String descripcion;

    @Column(name = "localizacion_id", nullable = false)
    private Long localizacionId;

    // @ManyToOne
    // @JoinColumn(name = "localizacion_id", referencedColumnName = "id", insertable
    // = false, updatable = false)
    // private Localizacion localizacion; // TODO falta todo paquete localizacion
    // (julian)

    @Column(name = "fecha_limite_inscripcion", nullable = false)
    private LocalDate fechaLimiteInscripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "min_participantes", nullable = false)
    private int minParticipantes;

    @Column(name = "max_participantes", nullable = false)
    private int maxParticipantes;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    // @ManyToOne
    // @JoinColumn(name = "entidad_id", referencedColumnName = "id", insertable =
    // false, updatable = false)
    // private Entidad entidad; // TODO falta todo paquete entidad salud(julian)

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCampana estado;
}
