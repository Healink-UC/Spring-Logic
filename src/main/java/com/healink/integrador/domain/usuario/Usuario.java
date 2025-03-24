package com.healink.integrador.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "USUARIOS")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_identificacion", nullable = false, length = 3)
    private String tipoIdentificacion;

    @Column(name = "identificacion", nullable = false)
    private String identificacion;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "correo", unique = true)
    private String correo;

    @Column(name = "clave", nullable = false)
    private String clave;

    @Column(name = "celular")
    private String celular;

    @Column(name = "esta_activo")
    private Boolean estaActivo = true;

    @Column(name = "rol_id", nullable = false)
    private Long rolId;

    // @Column(name = "ultimo_acceso")
    // private LocalDateTime ultimoAcceso;

    // @Column(name = "fecha_registro")
    // private LocalDate fechaRegistro = LocalDate.now();

}