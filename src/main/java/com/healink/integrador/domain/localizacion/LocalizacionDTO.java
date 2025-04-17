package com.healink.integrador.domain.localizacion;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocalizacionDTO implements DTOBase {
    private Long id;

    @NotNull(message = "El campo departamento es obligatorio")
    private String departamento;
    @NotNull(message = "El campo municipio es obligatorio")
    private String municipio;

    @NotNull(message = "El campo vereda es obligatorio")
    private String vereda;

    @NotNull(message = "El campo localidad es obligatorio")
    private String localidad;

    @NotNull(message = "El campo latitud es obligatorio")
    private Double latitud;

    @NotNull(message = "El campo longitud es obligatorio")
    private Double longitud;
}
