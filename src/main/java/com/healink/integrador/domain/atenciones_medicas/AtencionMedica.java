package com.healink.integrador.domain.atenciones_medicas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Optional;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedica;

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

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoAtencionMedica estado;

    public Optional<AtencionMedica> map(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citacion_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CitacionMedica citacionMedica;
}
