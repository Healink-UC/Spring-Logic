package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.dto.DTOBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CitacionMedicaDTO implements DTOBase {

    @Schema(readOnly = true, description = "ID de la cita médica")
    private Long id;

    @Schema(description = "ID del paciente")
    private Long pacienteId;

    @Schema(description = "ID de la campaña")
    private Long campanaId;

    @Schema(description = "ID del médico")
    private Long medicoId;

    @Schema(description = "Hora programada de la cita")
    private LocalDateTime horaProgramada;

    @Schema(description = "Hora de inicio de atención")
    private LocalDateTime horaAtencion;

    @Schema(description = "Hora de fin de atención")
    private LocalDateTime horaFinAtencion;

    @Schema(description = "Duración estimada en minutos")
    private Integer duracionEstimada;

    @Schema(description = "Estado de la cita")
    private String estado;

    @Schema(description = "Predicción de asistencia (0-100%)")
    private BigDecimal prediccionAsistencia;

    @Schema(description = "Código del ticket")
    private String codigoTicket;

    @Schema(description = "Notas adicionales")
    private String notas;
}