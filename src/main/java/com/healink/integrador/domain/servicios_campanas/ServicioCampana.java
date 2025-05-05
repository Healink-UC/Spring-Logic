package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.campana.Campana;
import com.healink.integrador.domain.servicios_medicos.ServicioMedico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SERVICIOS_CAMPANAS")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ServicioCampana extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "servicio_id", nullable = false)
    private Long servicioId;

    @Column(name = "campana_id", nullable = false)
    private Long campanaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ServicioMedico servicioMedico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campana_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Campana campana;
}
