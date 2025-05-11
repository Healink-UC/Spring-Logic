package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.dto.DTOBase;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El campo hora programada es requerido")
    private LocalDateTime horaProgramada;

    @NotNull(message = "El campo hora atención es requerido")
    private LocalDateTime horaAtencion;

    @NotNull(message = "El campo duración estimada es requerido")
    private Integer duracionEstimada; // en minutos

    @NotNull(message = "El campo estado es requerido")
    private String estado;

    @NotNull(message = "El campo predicción asistencia es requerido")
    private BigDecimal prediccionAsistencia; // 0-100%

    @NotNull(message = "El campo codigoTicket es requerido")
    private String codigoTicket;  // 1-5

    @NotNull(message = "El campo notas es requerido")
    private String notas;
}