package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmbajadorDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El nombre completo es requerido")
    private String nombreCompleto;

    @NotNull(message = "El teléfono es requerido")
    private String telefono;

    @NotNull(message = "El ID de la entidad es requerida")
    private Long entidadId;

    @NotNull(message = "El ID del usuario es requerido")
    private Long usuarioId;

    @NotNull(message = "La fecha registro es requerida")
    private LocalDate fechaRegistro = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ACTIVO;
}
