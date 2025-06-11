package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EntidadSaludDTO implements DTOBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La razón social es requerida")
    private String razonSocial;

    @NotNull(message = "La dirección es requerida")
    private String direccion;

    @NotNull(message = "El teléfono es requerido")
    private String telefono;

    @NotNull(message = "El correo es requerido")
    private String correo;

    @NotNull(message = "El usuario es requerido")
    private Long usuarioId;
}
