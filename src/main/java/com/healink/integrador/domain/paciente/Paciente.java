package com.healink.integrador.domain.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.healink.integrador.core.entity.EntidadAuditable;

@Entity
@Table(name = "PACIENTES")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private GeneroBiologico genero;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "tipo_sangre", length = 20)
    @Enumerated(EnumType.STRING)
    private TipoSangre tipoSangre;

    @Column(name = "localizacion_id")
    private Long localizacionId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

}
