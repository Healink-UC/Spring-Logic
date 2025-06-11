package com.healink.integrador.domain.seguimientos;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/seguimientos")
@Tag(name = "Seguimientos", description = "API para gestión de seguimientos")
public class SeguimientoController extends ControladorGenerico<Seguimiento, SeguimientoDTO> {

    private static final Logger logger = LoggerFactory.getLogger(SeguimientoController.class);
    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService, SeguimientoMapper seguimientoMapper) {
        super(seguimientoService, seguimientoMapper);
        this.seguimientoService = seguimientoService;
    }

    @GetMapping("/atencion/{atencion_id}")
    @Operation(summary = "Buscar seguimientos por atención médica", description = "Obtiene todos los seguimientos de una atención médica específica")
    public ResponseEntity<List<SeguimientoDTO>> buscarPorAtencionId(@PathVariable Long atencion_id) {
        List<Seguimiento> seguimientos = seguimientoService.buscarPorAtencionId(atencion_id);
        return ResponseEntity.ok(mapeador.aListaDTO(seguimientos));
    }

    @GetMapping("/atencion/{atencion_id}/paginado")
    @Operation(summary = "Buscar seguimientos por atención médica (paginado)", description = "Obtiene todos los seguimientos de una atención médica específica paginada")
    public ResponseEntity<Page<SeguimientoDTO>> buscarPorAtencionIdPaginado(
            @PathVariable Long atencion_id, Pageable pageable) {
        Page<Seguimiento> pagina = seguimientoService.buscarPorAtencionId(atencion_id, pageable);
        return ResponseEntity.ok(pagina.map(seguimiento -> mapeador.aDTO(seguimiento)));
    }

    /**
     * NUEVO: Obtener seguimientos pendientes de un paciente
     */
    @GetMapping("/paciente/{pacienteId}/pendientes")
    @Operation(summary = "Obtener seguimientos pendientes de un paciente", description = "Retorna los seguimientos que debe completar el paciente hoy o que están vencidos")
    public ResponseEntity<List<SeguimientoDTO>> obtenerSeguimientosPendientes(@PathVariable Long pacienteId) {
        List<Seguimiento> seguimientos = seguimientoService.obtenerSeguimientosPendientesPorPaciente(pacienteId);
        return ResponseEntity.ok(mapeador.aListaDTO(seguimientos));
    }

    /**
     * NUEVO: Obtener seguimientos disponibles HOY para un paciente
     */
    @GetMapping("/paciente/{pacienteId}/disponibles-hoy")
    @Operation(summary = "Obtener seguimientos disponibles hoy", description = "Retorna los seguimientos disponibles hoy para que el paciente complete cuestionarios")
    public ResponseEntity<List<SeguimientoDTO>> obtenerSeguimientosDisponiblesHoy(@PathVariable Long pacienteId) {
        List<Seguimiento> seguimientos = seguimientoService.obtenerSeguimientosDisponiblesHoy(pacienteId);
        return ResponseEntity.ok(mapeador.aListaDTO(seguimientos));
    }

    /**
     * NUEVO: Obtener seguimientos por paciente (todos)
     */
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Obtener todos los seguimientos de un paciente", description = "Retorna todos los seguimientos asociados a un paciente")
    public ResponseEntity<List<SeguimientoDTO>> obtenerSeguimientosPorPaciente(@PathVariable Long pacienteId) {
        List<Seguimiento> seguimientos = seguimientoService.obtenerSeguimientosPorPaciente(pacienteId);
        return ResponseEntity.ok(mapeador.aListaDTO(seguimientos));
    }

    /**
     * NUEVO: Completar seguimiento con respuestas del cuestionario
     */
    @PutMapping("/{seguimientoId}/completar")
    @Operation(summary = "Completar un seguimiento", description = "Marca un seguimiento como completado y guarda las respuestas del cuestionario")
    public ResponseEntity<SeguimientoDTO> completarSeguimiento(
            @PathVariable Long seguimientoId,
            @RequestBody Map<String, Object> respuestasEvaluacion) {
        
        Seguimiento seguimientoCompletado = seguimientoService.completarSeguimiento(seguimientoId, respuestasEvaluacion);
        return ResponseEntity.ok(mapeador.aDTO(seguimientoCompletado));
    }

    /**
     * NUEVO: Generar cuestionario para un seguimiento específico
     */
    @PostMapping("/{seguimientoId}/generar-cuestionario")
    @Operation(summary = "Generar cuestionario personalizado", description = "Genera un cuestionario personalizado para el seguimiento usando n8n")
    public ResponseEntity<Map<String, Object>> generarCuestionario(@PathVariable Long seguimientoId) {
        Map<String, Object> cuestionario = seguimientoService.generarCuestionarioPersonalizado(seguimientoId);
        return ResponseEntity.ok(cuestionario);
    }

    /**
     * NUEVO: Generar cuestionario personalizado basado en el seguimiento específico
     */
    @PostMapping("/{seguimientoId}/generar-cuestionario-especifico")
    public ResponseEntity<Map<String, Object>> generarCuestionarioEspecifico(
            @PathVariable Long seguimientoId) {
        
        try {
            logger.info("🎯 Generando cuestionario específico para seguimiento: {}", seguimientoId);
            
            // Obtener seguimiento completo
            Seguimiento seguimiento = seguimientoService.obtenerPorId(seguimientoId);
            
            // Preparar datos para el workflow del compañero
            Map<String, Object> datosParaWorkflow = seguimientoService.prepararDatosParaWorkflowCompanero(seguimiento);
            
            // Llamar al workflow del compañero con n8n
            // TODO: Integrar con n8nIntegrationService cuando esté listo
            Map<String, Object> cuestionarioGenerado = seguimientoService.generarCuestionarioConWorkflowCompanero(datosParaWorkflow);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "seguimiento_id", seguimientoId,
                "cuestionario", cuestionarioGenerado,
                "contexto", datosParaWorkflow
            ));
            
        } catch (Exception e) {
            logger.error("❌ Error generando cuestionario específico: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            ));
        }
    }
}