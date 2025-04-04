package com.healink.integrador.domain.localizacion;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class LocalizacionDTO implements DTOBase {
    private Long id;

    private String departamento;

    private String municipio;

    private String vereda;

    private String localidad;

    private Double latitud;

    private Double longitud;
}
