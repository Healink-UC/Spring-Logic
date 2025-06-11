package com.healink.integrador.domain.interacciones_chatbot;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/interacciones-chatbot")
@Tag(name = "Interacciones con el chatbot", description = "API para gestión de interacciones con el chatbot")
public class InteraccionChatbotController extends ControladorGenerico<InteraccionChatbot, InteraccionChatbotDTO> {

    private final InteraccionChatbotService interaccionChatbotService;
    private final InteraccionChatbotMapper mapper;

    public InteraccionChatbotController(InteraccionChatbotService interaccionChatbotService, InteraccionChatbotMapper interaccionChatbotMapper) {
        super(interaccionChatbotService, interaccionChatbotMapper);
        this.interaccionChatbotService = interaccionChatbotService;
        this.mapper = interaccionChatbotMapper;
    }

    @GetMapping("/seguimiento/{seguimientoId}")
    @Operation(summary = "Buscar interacciones de acuerdo a un seguimiento", description = "Obtiene todas las interacciones de un seguimiento específico")
    public ResponseEntity<List<InteraccionChatbotDTO>> obtenerPorSeguimiento(
            @PathVariable Long seguimientoId) {
        
        List<InteraccionChatbot> interacciones = interaccionChatbotService.buscarPorSeguimientoId(seguimientoId);
        List<InteraccionChatbotDTO> interaccionesDTO = interacciones.stream()
                .map(mapper::aDTO)
                .toList();
        
        return ResponseEntity.ok(interaccionesDTO);
    }

    @GetMapping("/seguimiento/{seguimientoId}/paginado")
    @Operation(summary = "Buscar seguimientos por atención médica (paginado)", description = "Obtiene todos los seguimientos de una atención médica específica paginada")
    public ResponseEntity<Page<InteraccionChatbotDTO>> obtenerPorSeguimientoPaginado(
            @PathVariable Long seguimientoId,
            Pageable pageable) {
        
        Page<InteraccionChatbot> interacciones = interaccionChatbotService.buscarPorSeguimientoId(seguimientoId, pageable);
        Page<InteraccionChatbotDTO> interaccionesDTO = interacciones.map(mapper::aDTO);
        
        return ResponseEntity.ok(interaccionesDTO);
    }

    /**
     * NUEVO: Guardar respuestas del cuestionario
     */
    @PostMapping("/seguimiento/{seguimientoId}/respuestas")
    public ResponseEntity<Map<String, Object>> guardarRespuestasCuestionario(
            @PathVariable Long seguimientoId,
            @RequestBody Map<String, Object> respuestas) {
        
        try {
            InteraccionChatbot interaccion = interaccionChatbotService.guardarRespuestasCuestionario(seguimientoId, respuestas);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Respuestas guardadas correctamente",
                "interaccion_id", interaccion.getId(),
                "timestamp", interaccion.getFecha_hora()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error guardando respuestas: " + e.getMessage()
            ));
        }
    }

    /**
     * NUEVO: Analizar respuestas y obtener recomendaciones
     */
    @GetMapping("/seguimiento/{seguimientoId}/analisis")
    public ResponseEntity<Map<String, Object>> analizarRespuestas(
            @PathVariable Long seguimientoId) {
        
        try {
            Map<String, Object> analisis = interaccionChatbotService.analizarRespuestasCuestionario(seguimientoId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "analisis", analisis
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error analizando respuestas: " + e.getMessage()
            ));
        }
    }

    /**
     * NUEVO: Obtener análisis completo con IA (preparado para integración n8n)
     */
    @PostMapping("/seguimiento/{seguimientoId}/analizar-con-ia")
    public ResponseEntity<Map<String, Object>> analizarConIA(
            @PathVariable Long seguimientoId,
            @RequestBody(required = false) Map<String, Object> parametrosAdicionales) {
        
        try {
            // Por ahora usamos análisis básico, luego integraremos con n8n
            Map<String, Object> analisisBasico = interaccionChatbotService.analizarRespuestasCuestionario(seguimientoId);
            
            // TODO: Aquí se integrará con n8n para análisis más sofisticado con IA
            // Map<String, Object> analisisIA = n8nIntegrationService.analizarRespuestasConIA(seguimientoId, analisisBasico);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "analisis_basico", analisisBasico,
                "mensaje", "Análisis básico completado. Integración con IA pendiente."
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error en análisis con IA: " + e.getMessage()
            ));
        }
    }
}