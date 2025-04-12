package com.healink.integrador.domain.campana_factor;

import com.healink.integrador.core.entity.EntidadAuditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campana_factores")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CampanaFactores extends EntidadAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "campana_id", nullable = false)
    private Long campanaId;

    @Column(name = "factor_id", nullable = false)
    private Long factorId;

    // @ManyToOne
    // @JoinColumn(name = "campana_id", referencedColumnName = "id", insertable =
    // false, updatable = false)
    // private Campana campanas; //TODO wait to marge from campanas

    // @ManyToOne
    // @JoinColumn(name = "factor_id", referencedColumnName = "id", insertable =
    // false, updatable = false)
    // private FactorRiesgo factores;

}
