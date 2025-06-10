package com.healink.integrador.core.webhooks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.core.integrations.N8nIntegrationService;
import com.healink.integrador.domain.seguimientos.SeguimientoService;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para recibir webhooks desde n8n
 * RESPONSABILIDAD: Solo recepción de resultados desde n8n y logging básico
 */
@RestController
@RequestMapping("/api/webhooks/n8n")
@CrossOrigin(origins = "*")
public class N8nWebhookController {
    
    private static final Logger logger = LoggerFactory.getLogger(N8nWebhookController.class);
    
    private final N8nIntegrationService n8nIntegrationService;
    private final SeguimientoService seguimientoService;
    
    public N8nWebhookController(N8nIntegrationService n8nIntegrationService,
                               SeguimientoService seguimientoService) {
        this.n8nIntegrationService = n8nIntegrationService;
        this.seguimientoService = seguimientoService;
    }
    
    /**
     * Endpoint para recibir cuestionario generado por el agente conversacional
     */
    @PostMapping("/cuestionario-generado")
    public ResponseEntity<Map<String, Object>> recibirCuestionarioGenerado(@RequestBody Map<String, Object> payload) {
        try {
            logger.info("Cuestionario generado recibido desde n8n: {}", payload);
            
            Long pacienteId = extractLong(payload, "paciente_id");
            Integer numeroSeguimiento = extractInteger(payload, "seguimiento_numero");
            String cuestionarioHtml = (String) payload.get("cuestionario_html");
            String linkCuestionario = (String) payload.get("link_cuestionario");
            Map<String, Object> metadatos = (Map<String, Object>) payload.get("metadatos_cuestionario");
            
            // TODO: Aquí podrías guardar el cuestionario en BD si es necesario
            // O simplemente registrar que fue generado
            
            logger.info("Cuestionario procesado para paciente {} - seguimiento {}", pacienteId, numeroSeguimiento);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Cuestionario recibido correctamente"
            ));
            
        } catch (Exception e) {
            logger.error("Error procesando cuestionario generado: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "mensaje", "Error: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Endpoint para recibir análisis completado por los agentes analizador + recomendaciones
     */
    @PostMapping("/analisis-completado")
    public ResponseEntity<Map<String, Object>> recibirAnalisisCompletado(@RequestBody Map<String, Object> payload) {
        try {
            logger.info("Análisis completado recibido desde n8n: {}", payload);
            
            Long pacienteId = extractLong(payload, "paciente_id");
            Integer numeroSeguimiento = extractInteger(payload, "seguimiento_numero");
            Map<String, Object> analisis = (Map<String, Object>) payload.get("analisis");
            Map<String, Object> recomendaciones = (Map<String, Object>) payload.get("recomendaciones");
            
            // Verificar si requiere atención urgente
            Boolean requiereAtencionInmediata = (Boolean) analisis.get("requiere_atencion_inmediata");
            if (Boolean.TRUE.equals(requiereAtencionInmediata)) {
                logger.warn("🚨 ATENCIÓN INMEDIATA requerida para paciente {}", pacienteId);
                // El n8n ya debe haber enviado la notificación de urgencia
            }
            
            // Programar próximo seguimiento si es necesario
            Boolean programarSiguiente = (Boolean) payload.get("programar_siguiente_seguimiento");
            if (Boolean.TRUE.equals(programarSiguiente)) {
                logger.info("Próximo seguimiento programado para paciente {}", pacienteId);
                // TODO: Implementar lógica de programación de seguimientos
            }
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Análisis procesado correctamente"
            ));
            
        } catch (Exception e) {
            logger.error("Error procesando análisis completado: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "mensaje", "Error: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Endpoint para recibir confirmación de notificación enviada
     */
    @PostMapping("/notificacion-enviada")
    public ResponseEntity<Map<String, Object>> recibirNotificacionEnviada(@RequestBody Map<String, Object> payload) {
        try {
            logger.info("Notificación enviada: {}", payload);
            
            Long pacienteId = extractLong(payload, "paciente_id");
            String tipoNotificacion = (String) payload.get("tipo_notificacion");
            Boolean exitosa = (Boolean) payload.get("exitosa");
            String error = (String) payload.get("error");
            
            if (Boolean.TRUE.equals(exitosa)) {
                logger.info("Notificación {} enviada exitosamente a paciente {}", tipoNotificacion, pacienteId);
            } else {
                logger.error("Error enviando notificación {} a paciente {}: {}", tipoNotificacion, pacienteId, error);
            }
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Estado de notificación registrado"
            ));
            
        } catch (Exception e) {
            logger.error("Error registrando estado de notificación: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "mensaje", "Error: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Endpoint para healthcheck desde n8n
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "healthy",
            "timestamp", System.currentTimeMillis(),
            "service", "n8n-webhook-controller"
        ));
    }
    
    // Métodos auxiliares
    private Long extractLong(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        return null;
    }
    
    private Integer extractInteger(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        return null;
    }
} 