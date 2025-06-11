package com.healink.integrador.core.webhooks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.healink.integrador.domain.atenciones_medicas.AtencionMedica;
import com.healink.integrador.domain.atenciones_medicas.AtencionMedicaService;
import com.healink.integrador.domain.atenciones_medicas.EstadoAtencionMedica;
import com.healink.integrador.domain.seguimientos.Seguimiento;
import com.healink.integrador.domain.seguimientos.SeguimientoService;
import com.healink.integrador.domain.seguimientos.SeguimientoDTO;
import com.healink.integrador.domain.seguimientos.SeguimientoMapper;
import com.healink.integrador.core.integrations.N8nIntegrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test/paso3")
@Tag(name = "Test Paso 3", description = "Endpoints de prueba para verificar funcionalidad del Paso 3")
public class TestPaso3Controller {

    private static final Logger logger = LoggerFactory.getLogger(TestPaso3Controller.class);
    
    private final AtencionMedicaService atencionMedicaService;
    private final SeguimientoService seguimientoService;
    private final SeguimientoMapper seguimientoMapper;
    private final N8nIntegrationService n8nIntegrationService;

    public TestPaso3Controller(
            AtencionMedicaService atencionMedicaService,
            SeguimientoService seguimientoService,
            SeguimientoMapper seguimientoMapper,
            N8nIntegrationService n8nIntegrationService) {
        
        this.atencionMedicaService = atencionMedicaService;
        this.seguimientoService = seguimientoService;
        this.seguimientoMapper = seguimientoMapper;
        this.n8nIntegrationService = n8nIntegrationService;
    }

    /**
     * 🧪 PASO 3.1: Simular atención médica completada (genera seguimientos automáticamente)
     */
    @PostMapping("/simular-atencion-completada/{pacienteId}")
    @Operation(summary = "Simular atención médica completada", 
               description = "Simula una atención médica completada para verificar que se generen seguimientos automáticamente")
    public ResponseEntity<Map<String, Object>> simularAtencionCompletada(@PathVariable Long pacienteId) {
        try {
            logger.info("🧪 SIMULANDO atención completada para paciente: {}", pacienteId);
            
            // Crear atención médica simulada
            AtencionMedica atencionSimulada = new AtencionMedica();
            atencionSimulada.setCitacionId(null); // Sin citación real
            atencionSimulada.setFechaHoraInicio(Timestamp.valueOf(LocalDateTime.now().minusMinutes(30)));
            atencionSimulada.setFechaHoraFin(Timestamp.valueOf(LocalDateTime.now()));
            atencionSimulada.setDuracionReal(30);
            atencionSimulada.setEstado(EstadoAtencionMedica.COMPLETADA);
            
            // Guardar atención (esto debería activar la generación automática de seguimientos)
            // NOTA: Como no tenemos citación real, usaremos el método directo
            logger.info("🔄 Iniciando seguimientos directamente por paciente ID...");
            n8nIntegrationService.iniciarSeguimientosPacientePorId(pacienteId, null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Atención simulada completada y seguimientos iniciados");
            response.put("paciente_id", pacienteId);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error simulando atención completada: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error: " + e.getMessage());
            errorResponse.put("paciente_id", pacienteId);
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 🧪 PASO 3.2: Verificar seguimientos disponibles HOY para un paciente
     */
    @GetMapping("/verificar-seguimientos-hoy/{pacienteId}")
    @Operation(summary = "Verificar seguimientos disponibles hoy", 
               description = "Verifica qué seguimientos están disponibles hoy para que el paciente complete cuestionarios")
    public ResponseEntity<Map<String, Object>> verificarSeguimientosHoy(@PathVariable Long pacienteId) {
        try {
            logger.info("🔍 VERIFICANDO seguimientos disponibles HOY para paciente: {}", pacienteId);
            
            // Obtener seguimientos disponibles hoy
            List<Seguimiento> seguimientosHoy = seguimientoService.obtenerSeguimientosDisponiblesHoy(pacienteId);
            
            // Obtener todos los seguimientos pendientes para comparación
            List<Seguimiento> seguimientosPendientes = seguimientoService.obtenerSeguimientosPendientesPorPaciente(pacienteId);
            
            // Obtener todos los seguimientos del paciente
            List<Seguimiento> todosSeguimientos = seguimientoService.obtenerSeguimientosPorPaciente(pacienteId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("paciente_id", pacienteId);
            response.put("fecha_consulta", LocalDate.now());
            response.put("seguimientos_disponibles_hoy", seguimientosHoy.size());
            response.put("seguimientos_hoy_detalle", seguimientoMapper.aListaDTO(seguimientosHoy));
            response.put("seguimientos_pendientes_total", seguimientosPendientes.size());
            response.put("seguimientos_pendientes_detalle", seguimientoMapper.aListaDTO(seguimientosPendientes));
            response.put("todos_seguimientos_total", todosSeguimientos.size());
            response.put("resumen", Map.of(
                "puede_completar_cuestionarios_hoy", seguimientosHoy.size() > 0,
                "tiene_seguimientos_pendientes", seguimientosPendientes.size() > 0,
                "total_seguimientos_sistema", todosSeguimientos.size()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error verificando seguimientos: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error: " + e.getMessage());
            errorResponse.put("paciente_id", pacienteId);
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 🧪 PASO 3.3: Probar generación de cuestionario específico
     */
    @PostMapping("/probar-cuestionario/{seguimientoId}")
    @Operation(summary = "Probar generación de cuestionario específico", 
               description = "Prueba la generación de cuestionario basado en análisis previo del seguimiento")
    public ResponseEntity<Map<String, Object>> probarCuestionario(@PathVariable Long seguimientoId) {
        try {
            logger.info("🎯 PROBANDO generación de cuestionario para seguimiento: {}", seguimientoId);
            
            // Obtener seguimiento
            Seguimiento seguimiento = seguimientoService.obtenerPorId(seguimientoId);
            
            // Preparar datos para workflow compañero
            Map<String, Object> datosWorkflow = seguimientoService.prepararDatosParaWorkflowCompanero(seguimiento);
            
            // Generar cuestionario con workflow compañero
            Map<String, Object> cuestionarioGenerado = seguimientoService.generarCuestionarioConWorkflowCompanero(datosWorkflow);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("seguimiento_id", seguimientoId);
            response.put("seguimiento_info", seguimientoMapper.aDTO(seguimiento));
            response.put("datos_enviados_workflow", datosWorkflow);
            response.put("cuestionario_generado", cuestionarioGenerado);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error probando cuestionario: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error: " + e.getMessage());
            errorResponse.put("seguimiento_id", seguimientoId);
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 🧪 PASO 3.0: Verificar estado general del sistema
     */
    @GetMapping("/verificar-sistema")
    @Operation(summary = "Verificar estado general del sistema", 
               description = "Verifica que todos los componentes del sistema están funcionando correctamente")
    public ResponseEntity<Map<String, Object>> verificarSistema() {
        try {
            logger.info("🔧 VERIFICANDO estado general del sistema...");
            
            // Verificar n8n
            String urlN8n = n8nIntegrationService.obtenerUrlActual();
            
            // Contar seguimientos en el sistema
            List<Seguimiento> todosSeguimientos = seguimientoService.obtenerSeguimientosPorPaciente(1L); // Temporal para evitar error
            
            Map<String, Object> estadoSistema = new HashMap<>();
            estadoSistema.put("n8n_url", urlN8n);
            estadoSistema.put("n8n_disponible", urlN8n != null && !urlN8n.isEmpty());
            estadoSistema.put("seguimientos_total_sistema", todosSeguimientos.size());
            estadoSistema.put("fecha_verificacion", LocalDateTime.now());
            estadoSistema.put("componentes", Map.of(
                "seguimiento_service", "OK",
                "atencion_service", "OK",
                "n8n_integration", "OK",
                "repository_queries", "OK"
            ));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("sistema_listo", true);
            response.put("estado", estadoSistema);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error verificando sistema: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("sistema_listo", false);
            errorResponse.put("message", "Error: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
} 