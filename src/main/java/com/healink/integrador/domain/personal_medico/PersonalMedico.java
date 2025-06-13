package com.healink.integrador.domain.personal_medico;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.usuario.Usuario;
import com.healink.integrador.domain.entidades_salud.EntidadSalud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @Column(name = "entidad_id")
    private Long entidadId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidad_id", referencedColumnName = "id", insertable = false, updatable = false)
    private EntidadSalud entidadSalud;
} 