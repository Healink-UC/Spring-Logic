package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CitacionMedicaDTO implements DTOBase {

    private Long id;

    @NotNull(message = "El ID de paciente es requerido")
    private Long pacienteId;

    @NotNull(message = "El ID de la campana es requerido")
    private Long campanaId;

    @NotNull(message = "El ID del medico es requerido")
    private Long medicoId;

    private LocalDateTime horaProgramada;

    private LocalDateTime horaAtencion;

    private Integer duracionEstimada;  // en minutos

    private String estado;

    private BigDecimal prediccionAsistencia;  // 0-100%

    private Integer prioridad;  // 1-5

    private String notas;
}