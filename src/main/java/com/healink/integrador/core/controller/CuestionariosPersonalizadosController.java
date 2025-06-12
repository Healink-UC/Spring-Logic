package com.healink.integrador.core.controller;

import com.healink.integrador.core.integrations.N8nIntegrationService;
import com.healink.integrador.domain.seguimientos.SeguimientoService;
import com.healink.integrador.domain.seguimientos.SeguimientoRepository;
import com.healink.integrador.domain.seguimientos.Seguimiento;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

/**
 * 🏥 CONTROLLER PARA CUESTIONARIOS PERSONALIZADOS Y ANÁLISIS DE RESPUESTAS
 * 
 * Endpoints que integran los workflows mejorados de n8n:
 * 1. Generación de cuestionarios personalizados basados en datos clínicos
 * 2. Análisis especializado de respuestas de cuestionarios
 */
@RestController
@RequestMapping("/api/cuestionarios-personalizados")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CuestionariosPersonalizadosController {

    private final N8nIntegrationService n8nIntegrationService;
    private final SeguimientoService seguimientoService;
    private final SeguimientoRepository seguimientoRepository;

    /**
     * 🎯 GENERAR CUESTIONARIO PERSONALIZADO
     * 
     * Genera un cuestionario específico basado en:
     * - Diagnósticos actuales del paciente
     * - Medicamentos prescritos
     * - Factores de riesgo cardiovascular
     * - Contexto del seguimiento
     */
    @PostMapping("/generar")
    public ResponseEntity<Map<String, Object>> generarCuestionarioPersonalizado(
            @RequestParam Long pacienteId,
            @RequestParam Long seguimientoId) {
        
        try {
            log.info("🎯 Solicitando cuestionario personalizado - Paciente: {}, Seguimiento: {}", 
                     pacienteId, seguimientoId);
            
            // Usar el método existente del SeguimientoService
            Map<String, Object> cuestionarioResponse = seguimientoService.generarCuestionarioPersonalizado(seguimientoId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cuestionario personalizado generado exitosamente");
            response.put("cuestionario", cuestionarioResponse);
            response.put("paciente_id", pacienteId);
            response.put("seguimiento_id", seguimientoId);
            response.put("timestamp", LocalDateTime.now());
            
            log.info("✅ Cuestionario personalizado generado exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Error generando cuestionario personalizado: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al generar cuestionario personalizado: " + e.getMessage());
            errorResponse.put("paciente_id", pacienteId);
            errorResponse.put("seguimiento_id", seguimientoId);
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 🧠 ANALIZAR RESPUESTAS DE CUESTIONARIO
     * 
     * Realiza un análisis especializado de las respuestas considerando:
     * - Respuestas del cuestionario actual
     * - Historial clínico completo
     * - Seguimientos anteriores de la campaña
     * - Análisis de riesgo cardiovascular de FastAPI
     * - Evolución temporal del paciente
     */
    @PostMapping("/analizar-respuestas")
    public ResponseEntity<Map<String, Object>> analizarRespuestas(
            @RequestBody Map<String, Object> request) {
        
        try {
            // Extraer parámetros de la solicitud
            Long pacienteId = extractLong(request, "paciente_id");
            Long seguimientoId = extractLong(request, "seguimiento_id");
            Map<String, Object> respuestas = (Map<String, Object>) request.get("respuestas");
            
            log.info("🧠 Iniciando análisis de respuestas - Paciente: {}, Seguimiento: {}", 
                     pacienteId, seguimientoId);
            
            // Validar parámetros requeridos
            if (pacienteId == null || seguimientoId == null || respuestas == null) {
                throw new IllegalArgumentException("paciente_id, seguimiento_id y respuestas son requeridos");
            }
            
            // Usar el método existente del SeguimientoService para completar seguimiento
            Seguimiento seguimientoActualizado = seguimientoService.completarSeguimiento(seguimientoId, respuestas);
            
            // TODO: Aquí se integrará el nuevo workflow de análisis de n8n cuando esté listo
            // Por ahora, usar el resultado del seguimiento completado
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Análisis de respuestas completado exitosamente");
            response.put("seguimiento_actualizado", Map.of(
                "id", seguimientoActualizado.getId(),
                "estado", seguimientoActualizado.getEstado(),
                "fecha_completado", seguimientoActualizado.getFecha_realizada(),
                "resultado", seguimientoActualizado.getResultado()
            ));
            response.put("paciente_id", pacienteId);
            response.put("seguimiento_id", seguimientoId);
            response.put("timestamp", LocalDateTime.now());
            
            log.info("✅ Análisis de respuestas completado exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Error analizando respuestas: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al analizar respuestas: " + e.getMessage());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 📊 OBTENER ANÁLISIS DE SEGUIMIENTOS DE UN PACIENTE
     * 
     * Devuelve todos los análisis de seguimientos completados para mostrar en el dashboard
     */
    @GetMapping("/paciente/{pacienteId}/analisis")
    public ResponseEntity<Map<String, Object>> obtenerAnalisisPaciente(
            @PathVariable Long pacienteId,
            @RequestParam(required = false) Long campanaId) {
        
        try {
            log.info("📊 Obteniendo análisis de seguimientos - Paciente: {}", pacienteId);
            
            // Obtener seguimientos del paciente usando el servicio existente
            var seguimientos = seguimientoService.obtenerSeguimientosPorPaciente(pacienteId);
            
            // Filtrar solo los completados
            var seguimientosCompletados = seguimientos.stream()
                .filter(s -> "COMPLETADO".equals(s.getEstado()) || s.getFecha_realizada() != null)
                .toList();
            
            // Construir respuesta con análisis
            var analisis = seguimientosCompletados.stream()
                .map(s -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("seguimiento_id", s.getId());
                    item.put("fecha_analisis", s.getFecha_realizada() != null ? s.getFecha_realizada() : s.getFecha_programada());
                    item.put("tipo", s.getTipo().toString());
                    item.put("prioridad", s.getPrioridad() != null ? s.getPrioridad().toString() : "MEDIA");
                    item.put("resultado", s.getResultado());
                    item.put("notas", s.getNotas());
                    
                    // Información básica para el dashboard
                    item.put("resumen_estado", extraerResumenEstado(s));
                    item.put("nivel_riesgo", extraerNivelRiesgo(s));
                    item.put("adherencia", extraerAdherencia(s));
                    item.put("requiere_atencion_urgente", evaluarUrgencia(s));
                    
                    return item;
                })
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("paciente_id", pacienteId);
            response.put("total_analisis", analisis.size());
            response.put("analisis", analisis);
            response.put("timestamp", LocalDateTime.now());
            
            log.info("✅ Obtenidos {} análisis para paciente {}", analisis.size(), pacienteId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Error obteniendo análisis del paciente: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error al obtener análisis: " + e.getMessage());
            errorResponse.put("paciente_id", pacienteId);
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 🧪 ENDPOINT DE PRUEBA PARA VALIDAR WORKFLOWS
     */
    @PostMapping("/test-workflows")
    public ResponseEntity<Map<String, Object>> testWorkflows(@RequestParam Long pacienteId) {
        try {
            log.info("🧪 Probando workflows con paciente: {}", pacienteId);
            
            Map<String, Object> response = new HashMap<>();
            
            // 1. Probar generación de cuestionario usando seguimientos existentes
            try {
                var seguimientos = seguimientoService.obtenerSeguimientosPendientesPorPaciente(pacienteId);
                
                if (!seguimientos.isEmpty()) {
                    Long seguimientoId = seguimientos.get(0).getId();
                    Map<String, Object> cuestionario = seguimientoService.generarCuestionarioPersonalizado(seguimientoId);
                    response.put("test_cuestionario", "SUCCESS");
                    response.put("cuestionario_sample", cuestionario);
                } else {
                    response.put("test_cuestionario", "NO_SEGUIMIENTOS_PENDIENTES");
                }
                
            } catch (Exception e) {
                response.put("test_cuestionario", "ERROR: " + e.getMessage());
            }
            
            // 2. Información de seguimientos disponibles
            try {
                var seguimientosPendientes = seguimientoService.obtenerSeguimientosPendientesPorPaciente(pacienteId);
                var seguimientosCompletos = seguimientoService.obtenerSeguimientosPorPaciente(pacienteId);
                
                response.put("seguimientos_pendientes", seguimientosPendientes.size());
                response.put("seguimientos_totales", seguimientosCompletos.size());
                
            } catch (Exception e) {
                response.put("info_seguimientos", "ERROR: " + e.getMessage());
            }
            
            response.put("success", true);
            response.put("message", "Test de workflows completado");
            response.put("paciente_id", pacienteId);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ Error en test de workflows: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage(),
                "paciente_id", pacienteId
            ));
        }
    }

    // ===== MÉTODOS AUXILIARES =====
    
    /**
     * Método auxiliar para extraer Long de Map
     */
    private Long extractLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Extraer resumen del estado del seguimiento
     */
    private String extraerResumenEstado(Seguimiento seguimiento) {
        String resultado = seguimiento.getResultado();
        if (resultado != null && resultado.length() > 100) {
            return resultado.substring(0, 97) + "...";
        }
        return resultado != null ? resultado : "Seguimiento completado";
    }

    /**
     * Extraer nivel de riesgo desde las notas del seguimiento
     */
    private String extraerNivelRiesgo(Seguimiento seguimiento) {
        String notas = seguimiento.getNotas();
        if (notas != null) {
            if (notas.contains("ALTO") || notas.contains("CRITICO")) return "ALTO";
            if (notas.contains("MODERADO") || notas.contains("MEDIO")) return "MODERADO";
            if (notas.contains("BAJO")) return "BAJO";
        }
        return "MODERADO"; // Default
    }

    /**
     * Extraer información de adherencia
     */
    private String extraerAdherencia(Seguimiento seguimiento) {
        String resultado = seguimiento.getResultado();
        if (resultado != null) {
            if (resultado.toLowerCase().contains("excelente") || resultado.toLowerCase().contains("muy buena")) {
                return "EXCELENTE";
            }
            if (resultado.toLowerCase().contains("buena") || resultado.toLowerCase().contains("adherente")) {
                return "BUENA";
            }
            if (resultado.toLowerCase().contains("regular") || resultado.toLowerCase().contains("parcial")) {
                return "REGULAR";
            }
            if (resultado.toLowerCase().contains("mala") || resultado.toLowerCase().contains("deficiente")) {
                return "DEFICIENTE";
            }
        }
        return "EVALUADA";
    }

    /**
     * Evaluar si requiere atención urgente
     */
    private boolean evaluarUrgencia(Seguimiento seguimiento) {
        String notas = seguimiento.getNotas();
        String resultado = seguimiento.getResultado();
        
        if (notas != null) {
            String notasLower = notas.toLowerCase();
            if (notasLower.contains("urgente") || notasLower.contains("critico") || 
                notasLower.contains("alarma") || notasLower.contains("emergencia")) {
                return true;
            }
        }
        
        if (resultado != null) {
            String resultadoLower = resultado.toLowerCase();
            if (resultadoLower.contains("síntoma") && resultadoLower.contains("grave")) {
                return true;
            }
        }
        
        return false;
    }
} 