package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


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

    private String identificacion = "";

    private String correo = "";

    @NotNull(message = "La localidad es requerida")
    private String localidad;

}
