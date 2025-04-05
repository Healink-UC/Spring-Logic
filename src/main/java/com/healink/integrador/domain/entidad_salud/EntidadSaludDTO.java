package com.healink.integrador.domain.entidad_salud;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EntidadSaludDTO implements DTOBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La razón social es requerida")
    private String razonSocial;

    @NotNull(message = "La dirección es requerida")
    private String direccion;

    @NotNull(message = "El telefono es requerido")
    private String telefono;

    @NotNull(message = "El ID de usuario es requerido")
    private Long usuarioId;

    private LocalDate fechaRegistro = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private EntidadSalud.Estado estado = EntidadSalud.Estado.ACTIVO;
}
