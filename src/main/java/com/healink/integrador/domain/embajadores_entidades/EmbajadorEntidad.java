package com.healink.integrador.domain.embajadores_entidades;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.embajadores.Embajador;
import com.healink.integrador.domain.entidades_salud.EntidadSalud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EMBAJADORES_ENTIDADES")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class EmbajadorEntidad extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "embajador_id", nullable = false)
    private Long embajadorId;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "embajador_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Embajador embajador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidad_id", referencedColumnName = "id", insertable = false, updatable = false)
    private EntidadSalud entidadSalud;
} 