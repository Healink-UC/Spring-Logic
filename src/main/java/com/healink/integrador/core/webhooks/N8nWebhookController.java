package com.healink.integrador.core.webhooks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.domain.seguimientos.SeguimientoService;
import com.healink.integrador.core.integrations.N8nIntegrationService;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para recibir webhooks desde n8n
 * RESPONSABILIDAD: Solo recepción de seguimientos generados por n8n
 */
@RestController
@RequestMapping("/api/webhooks/n8n")
@CrossOrigin(origins = "*")
public class N8nWebhookController {
    
    private static final Logger logger = LoggerFactory.getLogger(N8nWebhookController.class);
    
    private final SeguimientoService seguimientoService;
    private final N8nIntegrationService n8nIntegrationService;
    
    public N8nWebhookController(SeguimientoService seguimientoService, N8nIntegrationService n8nIntegrationService) {
        this.seguimientoService = seguimientoService;
        this.n8nIntegrationService = n8nIntegrationService;
    }
    
    /**
     * Endpoint principal para recibir seguimientos generados por n8n
     */
    @PostMapping("/seguimientos-generados")
    public ResponseEntity<Map<String, Object>> recibirSeguimientosGenerados(@RequestBody Map<String, Object> payload) {
        try {
            logger.info("🎯 Seguimientos generados desde n8n para paciente: {}", payload.get("pacienteId"));
            
            // Procesar seguimientos con el SeguimientoService
            Map<String, Object> resultado = seguimientoService.procesarSeguimientosDesdeN8n(payload);
            
            // Log del resultado
            logger.info("✅ Seguimientos procesados: {}", resultado.get("seguimientosCreados"));
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Seguimientos recibidos y procesados correctamente",
                "detalles", resultado
            ));
            
        } catch (Exception e) {
            logger.error("❌ Error procesando seguimientos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "mensaje", "Error: " + e.getMessage()
            ));
        }
    }
    
    /**
     * ENDPOINT PARA N8N: Recibir cuestionario/seguimientos generados (endpoint esperado por n8n)
     */
    @PostMapping("/cuestionario-generado")
    public ResponseEntity<Map<String, Object>> recibirCuestionarioGenerado(@RequestBody Map<String, Object> payload) {
        try {
            logger.info("🎯 Cuestionario/Seguimientos recibidos desde n8n para paciente: {}", payload.get("pacienteId"));
            
            // Mostrar estructura del payload para debugging
            logger.debug("📋 Estructura del payload: {}", payload.keySet());
            
            // Procesar seguimientos con el SeguimientoService actualizado
            Map<String, Object> resultado = seguimientoService.procesarSeguimientosDesdeN8n(payload);
            
            // Log detallado del resultado
            logger.info("✅ Resultado procesamiento: {}", resultado.get("status"));
            logger.info("📊 Seguimientos creados: {}", resultado.get("seguimientosCreados"));
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Cuestionario/Seguimientos recibidos y procesados correctamente",
                "detalles", resultado
            ));
            
        } catch (Exception e) {
            logger.error("❌ Error procesando cuestionario generado: {}", e.getMessage(), e);
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
    
    /**
     * ENDPOINT DE PRUEBA: Probar integración con datos reales de un paciente
     */
    @PostMapping("/test-paciente/{pacienteId}")
    public ResponseEntity<Map<String, Object>> testIntegracionPacienteReal(@PathVariable Long pacienteId) {
        try {
            logger.info("🧪 INICIANDO PRUEBA CON PACIENTE REAL ID: {}", pacienteId);
            
            // Usar el servicio de integración para iniciar seguimientos con datos reales
            n8nIntegrationService.iniciarSeguimientosPacientePorId(pacienteId, null);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "✅ Prueba iniciada con datos REALES del paciente " + pacienteId,
                "timestamp", System.currentTimeMillis(),
                "paciente_id", pacienteId,
                "modo", "test_datos_reales",
                "notas", "Los seguimientos se crearán automáticamente cuando n8n responda"
            ));
            
        } catch (Exception e) {
            logger.error("❌ Error en prueba con paciente real {}: {}", pacienteId, e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "mensaje", "Error al probar con paciente " + pacienteId + ": " + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }
} 