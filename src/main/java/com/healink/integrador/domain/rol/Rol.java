package com.healink.integrador.domain.rol;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "ROLES")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Rol extends EntidadAuditable {

    @Id

    private Long id;

    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;
}
