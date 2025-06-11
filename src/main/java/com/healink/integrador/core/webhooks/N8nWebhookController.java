package com.healink.integrador.core.webhooks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.domain.seguimientos.SeguimientoService;

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
    
    public N8nWebhookController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }
    
    /**
     * Endpoint principal para recibir seguimientos generados por n8n
     */
    @PostMapping("/cuestionario-generado")
    public ResponseEntity<Map<String, Object>> recibirCuestionarioGenerado(@RequestBody Map<String, Object> payload) {
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
} 