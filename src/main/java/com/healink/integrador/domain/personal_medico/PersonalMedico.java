package com.healink.integrador.domain.personal_medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "PERSONAL_MEDICO")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PersonalMedico extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "especialidad")
    private String especialidad;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
}