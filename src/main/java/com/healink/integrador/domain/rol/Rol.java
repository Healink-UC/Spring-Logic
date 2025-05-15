package com.healink.integrador.domain.rol;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.entity.EntidadAuditable;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;

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

    @Column(name = "permisos", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private transient JsonNode permisos;
}
