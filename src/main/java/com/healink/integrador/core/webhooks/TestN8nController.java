package com.healink.integrador.core.webhooks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.core.integrations.N8nIntegrationService;
import com.healink.integrador.domain.atenciones_medicas.AtencionMedica;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para probar la integración con n8n
 */
@RestController
@RequestMapping("/api/test/n8n")
@CrossOrigin(origins = "*")
public class TestN8nController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestN8nController.class);
    
    private final N8nIntegrationService n8nIntegrationService;
    
    public TestN8nController(N8nIntegrationService n8nIntegrationService) {
        this.n8nIntegrationService = n8nIntegrationService;
    }
    
    /**
     * Probar conexión básica con n8n
     */
    @PostMapping("/ping")
    public ResponseEntity<Map<String, Object>> testConexion() {
        try {
            logger.info("Probando conexión con n8n...");
            
            // CAMBIO: Usar NULL para simular una atención real sin datos hardcodeados
            AtencionMedica atencionPrueba = new AtencionMedica();
            atencionPrueba.setId(null); // Permitir que se asigne automáticamente
            atencionPrueba.setCitacionId(null); // Sin citación para prueba básica
            atencionPrueba.setFechaCreacion(LocalDateTime.now());
            
            // Intentar disparo del orquestador
            n8nIntegrationService.iniciarSeguimientosPaciente(atencionPrueba);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Conexión con n8n enviada correctamente - sin datos hardcodeados",
                "timestamp", System.currentTimeMillis(),
                "modo", "test_basico"
            ));
            
        } catch (Exception e) {
            logger.error("Error probando conexión con n8n: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "mensaje", "Error conectando con n8n: " + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * CAMBIO: Método especial para N8N Cloud gratuito SIN datos hardcodeados
     * Incluye reintentos y manejo de errores 404
     */
    @PostMapping("/ping-cloud")
    public ResponseEntity<Map<String, Object>> testConexionCloud() {
        Map<String, Object> response = new HashMap<>();
        int maxReintentos = 3;
        
        for (int intento = 1; intento <= maxReintentos; intento++) {
            try {
                logger.info("Intento {} de {} probando conexión con n8n cloud...", intento, maxReintentos);
                
                // CAMBIO: Sin datos hardcodeados
                AtencionMedica atencionPrueba = new AtencionMedica();
                atencionPrueba.setId(null); // Sin ID hardcodeado
                atencionPrueba.setCitacionId(null); // Sin citación hardcodeada
                atencionPrueba.setFechaCreacion(LocalDateTime.now());
                
                // Intentar disparo del orquestador
                n8nIntegrationService.iniciarSeguimientosPaciente(atencionPrueba);
                
                response.put("status", "success");
                response.put("mensaje", "Conexión exitosa con n8n cloud en intento " + intento + " - sin datos hardcodeados");
                response.put("timestamp", System.currentTimeMillis());
                response.put("modo", "test_cloud");
                response.put("intentos_realizados", intento);
                
                return ResponseEntity.ok(response);
                
            } catch (Exception e) {
                logger.warn("Intento {} falló: {}", intento, e.getMessage());
                
                if (intento == maxReintentos) {
                    // Último intento falló
                    response.put("status", "error");
                    response.put("mensaje", "Error después de " + maxReintentos + " intentos: " + e.getMessage());
                    response.put("timestamp", System.currentTimeMillis());
                    response.put("intentos_realizados", intento);
                    response.put("ultimo_error", e.getMessage());
                    response.put("solucion_sugerida", "1. Verifica que el workflow esté ACTIVADO en n8n, 2. Considera usar n8n self-hosted para eliminar limitaciones");
                    
                    return ResponseEntity.status(500).body(response);
                } else {
                    // Esperar antes del siguiente intento
                    try {
                        Thread.sleep(2000); // 2 segundos
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        return ResponseEntity.status(500).body(response);
    }
    
    /**
     * CAMBIO PRINCIPAL: Probar con datos REALES de un paciente específico - SIN hardcodeo
     */
    @PostMapping("/test-paciente/{pacienteId}")
    public ResponseEntity<Map<String, Object>> testConPacienteReal(@PathVariable Long pacienteId) {
        try {
            logger.info("=== PRUEBA CON DATOS REALES - PACIENTE ID: {} ===", pacienteId);
            
            // CAMBIO: Crear atención médica SIN datos hardcodeados que referencie al paciente real
            AtencionMedica atencionReal = new AtencionMedica();
            // NO asignar ID hardcodeado - dejar que se genere automáticamente si es necesario
            atencionReal.setId(null); 
            atencionReal.setCitacionId(null); // Sin citación hardcodeada
            atencionReal.setFechaCreacion(LocalDateTime.now());
            
            logger.info("Iniciando seguimientos para paciente REAL ID: {} - SIN datos hardcodeados", pacienteId);
            
            // Llamar directamente al método con el paciente ID real
            testSeguimientoPacienteReal(pacienteId, atencionReal);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "✅ Prueba iniciada con datos REALES del paciente " + pacienteId + " - SIN hardcodeo",
                "timestamp", System.currentTimeMillis(),
                "paciente_id", pacienteId,
                "modo", "datos_reales",
                "notas", "Sin datos hardcodeados - usando datos reales de BD"
            ));
            
        } catch (Exception e) {
            logger.error("Error probando con paciente real {}: {}", pacienteId, e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "mensaje", "Error al probar con paciente " + pacienteId + ": " + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }
    
    /**
     * CAMBIO: Método helper para probar seguimientos con paciente real - SIN hardcodeo - SIMPLIFICADO
     */
    private void testSeguimientoPacienteReal(Long pacienteId, AtencionMedica atencionReal) {
        try {
            logger.info("=== INICIANDO PRUEBA SIMPLIFICADA CON PACIENTE REAL {} ===", pacienteId);
            
            // CAMBIO: Usar el nuevo método directo del servicio - mucho más simple
            n8nIntegrationService.iniciarSeguimientosPacientePorId(pacienteId, null);
            
            logger.info("✅ Seguimientos con datos 100% reales iniciados para paciente {} usando método directo", pacienteId);
            
        } catch (Exception e) {
            logger.error("Error en seguimiento de paciente real {}: {}", pacienteId, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Verificar configuración de n8n
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> verificarConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // NUEVO: Mostrar la URL actual que está usando
        String urlActual = n8nIntegrationService.obtenerUrlActual();
        config.put("n8n_base_url_actual", urlActual);
        config.put("url_completa_orquestador", urlActual + "/orquestador-seguimientos");
        
        config.put("endpoints_disponibles", Map.of(
            "orquestador", "/webhook/orquestador-seguimientos"
        ));
        config.put("callbacks_esperados", Map.of(
            "cuestionario_generado", "/api/webhooks/n8n/cuestionario-generado",
            "analisis_completado", "/api/webhooks/n8n/analisis-completado",
            "notificacion_enviada", "/api/webhooks/n8n/notificacion-enviada"
        ));
        config.put("limitaciones_n8n_cloud", Map.of(
            "version_gratuita", "Los webhooks pueden requerir reactivación manual",
            "solucion_recomendada", "Usar n8n self-hosted para eliminar limitaciones",
            "endpoint_especial", "/api/test/n8n/ping-cloud (con reintentos)"
        ));
        
        return ResponseEntity.ok(config);
    }
} 