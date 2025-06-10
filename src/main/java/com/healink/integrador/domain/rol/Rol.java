package com.healink.integrador.domain.rol;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.entity.EntidadAuditable;



import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "ROLES")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Rol extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "permisos", columnDefinition = "jsonb")
    private JsonNode permisos;
}
