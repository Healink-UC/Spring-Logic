package com.healink.integrador.domain.embajadores_entidades;

import com.healink.integrador.core.dto.DTOBase;
import com.healink.integrador.domain.embajadores.EmbajadorDTO;
import com.healink.integrador.domain.entidades_salud.EntidadSaludDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmbajadorEntidadDTO implements DTOBase {
    private Long id;

    @NotNull(message = "El ID del embajador es requerido")
    private Long embajadorId;

    @NotNull(message = "El ID de la entidad es requerido")
    private Long entidadId;

    private EmbajadorDTO embajador = null;
    private EntidadSaludDTO entidadSalud = null;
    // Campos adicionales para mostrar información relacionada
    private String nombreEmbajador;
    private String nombreEntidad;
} 