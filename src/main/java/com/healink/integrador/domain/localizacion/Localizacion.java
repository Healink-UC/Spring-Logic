package com.healink.integrador.domain.localizacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "LOCALIZACIONES")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Localizacion extends EntidadAuditable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "departamento", nullable = false)
    private String departamento;

    @Column(name = "municipio", nullable = false)
    private String municipio;

    @Column(name = "vereda", nullable = false)
    private String vereda;

    @Column(name = "localidad", nullable = false)
    private String localidad;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;
}
