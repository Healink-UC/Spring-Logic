package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.service.ServicioGenerico;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CitacionMedicaService extends ServicioGenerico<CitacionMedica> {

    private static final Logger logger = LoggerFactory.getLogger(CitacionMedicaService.class);
    private final CitacionMedicaRepository citacionMedicaRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CitacionMedicaService(CitacionMedicaRepository citacionMedicaRepository,
                                ApplicationEventPublisher eventPublisher) {
        super(citacionMedicaRepository);
        this.citacionMedicaRepository = citacionMedicaRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public Optional<List<CitacionMedica>> getByPacienteId(Long pacienteId) {
        return citacionMedicaRepository.findByPacienteId(pacienteId);
    }

    @Transactional(readOnly = true)
    public Optional<List<CitacionMedica>> getByMedicoId(Long medicoId) {
        return citacionMedicaRepository.findByMedicoId(medicoId);
    }

    @Transactional(readOnly = true)
    public Optional<List<CitacionMedica>> getByCampanaId(Long campanaId) {
        return citacionMedicaRepository.findByCampanaId(campanaId);
    }

    /**
     * 🚀 NUEVO: Método para actualizar estado de citación con activación automática de seguimientos
     */
    @Transactional
    public CitacionMedica actualizarEstadoCitacion(Long citacionId, EstadoCitacion nuevoEstado) {
        logger.info("=== ACTUALIZANDO ESTADO DE CITACIÓN ===");
        logger.info("Citación ID: {}, Nuevo Estado: {}", citacionId, nuevoEstado);

        // Obtener citación actual
        CitacionMedica citacion = this.obtenerPorId(citacionId);
        if (citacion == null) {
            throw new RuntimeException("Citación no encontrada con ID: " + citacionId);
        }

        EstadoCitacion estadoAnterior = citacion.getEstado();
        logger.info("Estado anterior: {}", estadoAnterior);

        // Actualizar estado
        citacion.setEstado(nuevoEstado);
        
        // Si el nuevo estado es ATENDIDA, establecer hora de atención
        if (nuevoEstado == EstadoCitacion.ATENDIDA && citacion.getHoraAtencion() == null) {
            citacion.setHoraAtencion(LocalDateTime.now());
            logger.info("Hora de atención establecida: {}", citacion.getHoraAtencion());
        }

        // Guardar cambios
        CitacionMedica citacionActualizada = this.guardar(citacion);

        // 🎯 ACTIVACIÓN AUTOMÁTICA: Si cambia a ATENDIDA, iniciar seguimientos
        if (nuevoEstado == EstadoCitacion.ATENDIDA && estadoAnterior != EstadoCitacion.ATENDIDA) {
            logger.info("🚀 CITACIÓN ATENDIDA DETECTADA - Iniciando seguimientos automáticos...");
            
            try {
                // Publicar evento para evitar dependencia circular
                eventPublisher.publishEvent(new CitacionAtendidaEvent(citacionActualizada));
                logger.info("✅ Evento de citación atendida publicado para citación {}", citacionId);
                
            } catch (Exception e) {
                logger.error("❌ Error publicando evento para citación {}: {}", citacionId, e.getMessage(), e);
                // No lanzar excepción para no afectar la actualización de la citación
            }
        } else {
            logger.info("ℹ️ Estado actualizado pero no se requiere activación de seguimientos");
        }

        return citacionActualizada;
    }

    /**
     * 🩺 NUEVO: Marcar citación como atendida (método de conveniencia)
     */
    @Transactional
    public CitacionMedica marcarComoAtendida(Long citacionId) {
        logger.info("🩺 Marcando citación {} como ATENDIDA", citacionId);
        return actualizarEstadoCitacion(citacionId, EstadoCitacion.ATENDIDA);
    }

    /**
     * 📋 NUEVO: Endpoint para que los médicos marquen una citación como atendida
     */
    @Transactional
    public CitacionMedica completarAtencionMedica(Long citacionId, LocalDateTime horaReal) {
        logger.info("📋 Completando atención médica para citación {}", citacionId);
        
        CitacionMedica citacion = this.obtenerPorId(citacionId);
        if (citacion == null) {
            throw new RuntimeException("Citación no encontrada con ID: " + citacionId);
        }

        // Establecer hora real de atención si se proporciona
        if (horaReal != null) {
            citacion.setHoraAtencion(horaReal);
        }

        // Marcar como atendida (esto activará automáticamente los seguimientos)
        return actualizarEstadoCitacion(citacionId, EstadoCitacion.ATENDIDA);
    }
}