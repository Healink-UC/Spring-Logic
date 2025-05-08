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

    @NotNull(message = "El ID de usuario es requerido")
    private Long usuarioId;
}
