package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.campana.Campana;
import com.healink.integrador.domain.paciente.Paciente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CITACIONES_MEDICAS")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CitacionMedica extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "campana_id", nullable = false)
    private Long campanaId;

    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @Column(name = "hora_programada")
    private LocalDateTime horaProgramada;

    @Column(name = "hora_atencion", nullable = true)
    private LocalDateTime horaAtencion;

    @Column(name = "hora_fin_atencion", nullable = true)
    private LocalDateTime horaFinAtencion;

    @Column(name = "duracion_estimada")
    private Integer duracionEstimada;  // en minutos

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCitacion estado;

    @Column(name = "prediccion_asistencia", precision = 5, scale = 2)
    private BigDecimal prediccionAsistencia;  // 0-100%

    @Column(name = "codigo_ticket")
    private String codigoTicket;  // 1-5

    @Column(name = "notas")
    private String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campana_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Campana campana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Paciente paciente;
}