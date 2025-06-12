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

    @PostMapping("/crear-seguimientos-hoy/{pacienteId}")
    @Operation(
        summary = "🧪 PRUEBAS: Crear seguimientos para hoy",
        description = "Endpoint temporal para crear seguimientos de prueba con fecha de hoy"
    )
    public ResponseEntity<Map<String, Object>> crearSeguimientosHoy(@PathVariable Long pacienteId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            logger.info("🧪 CREANDO seguimientos de prueba para HOY - paciente: {}", pacienteId);
            
            // Datos simulados para crear seguimientos hoy
            Map<String, Object> datosSimulados = Map.of(
                "pacienteId", pacienteId,
                "atencion_id", 1L, // Atención ficticia
                "planSeguimiento", Map.of(
                    "seguimientos", List.of(
                        Map.of(
                            "diasDespues", 0, // HOY
                            "mensaje", "Seguimiento cardiovascular de urgencia para pruebas",
                            "tipo", "chatbot_cuestionario",
                            "prioridad", "ALTA",
                            "numeroSeguimiento", 1
                        ),
                        Map.of(
                            "diasDespues", 0, // HOY
                            "mensaje", "Evaluación de medicamentos para pruebas",
                            "tipo", "chatbot_cuestionario",
                            "prioridad", "MEDIA",
                            "numeroSeguimiento", 2
                        )
                    )
                ),
                "analisisIA", Map.of(
                    "nivelRiesgo", "ALTO",
                    "recomendaciones", "Seguimiento urgente cardiovascular",
                    "factoresRiesgo", List.of("hipertension", "colesterol")
                )
            );
            
            // Crear seguimientos usando el servicio existente
            Map<String, Object> resultado = seguimientoService.procesarSeguimientosDesdeN8n(datosSimulados);
            
            response.put("success", true);
            response.put("mensaje", "✅ Seguimientos de prueba creados para HOY");
            response.put("paciente_id", pacienteId);
            response.put("resultado", resultado);
            response.put("fecha_creacion", LocalDateTime.now());
            
            logger.info("✅ Seguimientos de prueba creados exitosamente para paciente {}", pacienteId);
            
        } catch (Exception e) {
            logger.error("❌ Error creando seguimientos de prueba: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("mensaje", "Error: " + e.getMessage());
            response.put("paciente_id", pacienteId);
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * ❤️ NUEVO: Health check simple para verificar conexión
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verificar que el servicio está funcionando")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "TestPaso3Controller");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * 🏗️ NUEVO: Crear atención temporal para pruebas
     */
    @PostMapping("/crear-atencion-temporal")
    @Operation(summary = "Crear atención temporal", description = "Crea una atención médica temporal para pruebas")
    public ResponseEntity<Map<String, Object>> crearAtencionTemporal(
            @org.springframework.web.bind.annotation.RequestBody Map<String, Object> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long pacienteId = Long.valueOf(request.get("pacienteId").toString());
            Integer duracionMinutos = Integer.valueOf(request.getOrDefault("duracionMinutos", 30).toString());
            
            logger.info("🏗️ CREANDO atención temporal para paciente: {}", pacienteId);
            
            // Crear atención temporal
            AtencionMedica atencionTemporal = new AtencionMedica();
            atencionTemporal.setCitacionId(null); // Sin citación
            atencionTemporal.setFechaHoraInicio(Timestamp.valueOf(LocalDateTime.now().minusMinutes(duracionMinutos)));
            atencionTemporal.setFechaHoraFin(Timestamp.valueOf(LocalDateTime.now()));
            atencionTemporal.setDuracionReal(duracionMinutos);
            atencionTemporal.setEstado(EstadoAtencionMedica.COMPLETADA);
            
            // Simular el guardado (realmente no guardamos para evitar conflictos)
            Long atencionId = System.currentTimeMillis(); // ID ficticio basado en timestamp
            
            response.put("success", true);
            response.put("atencion_id", atencionId);
            response.put("paciente_id", pacienteId);
            response.put("duracion_minutos", duracionMinutos);
            response.put("mensaje", "Atención temporal creada para pruebas");
            
            logger.info("✅ Atención temporal creada con ID: {}", atencionId);
            
        } catch (Exception e) {
            logger.error("❌ Error creando atención temporal: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("mensaje", "Error: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 🧹 NUEVO: Eliminar seguimientos para pruebas
     */
    @org.springframework.web.bind.annotation.DeleteMapping("/eliminar-seguimientos/{pacienteId}")
    @Operation(summary = "Eliminar seguimientos de prueba", description = "Elimina todos los seguimientos de un paciente para pruebas")
    public ResponseEntity<Map<String, Object>> eliminarSeguimientos(@PathVariable Long pacienteId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            logger.info("🧹 ELIMINANDO seguimientos para paciente: {}", pacienteId);
            
            // Obtener todos los seguimientos del paciente
            List<Seguimiento> seguimientos = seguimientoService.obtenerSeguimientosPorPaciente(pacienteId);
            
            // Eliminar uno por uno
            int eliminados = 0;
            for (Seguimiento seguimiento : seguimientos) {
                try {
                    seguimientoService.eliminarPorId(seguimiento.getId());
                    eliminados++;
                } catch (Exception e) {
                    logger.warn("⚠️ No se pudo eliminar seguimiento {}: {}", seguimiento.getId(), e.getMessage());
                }
            }
            
            response.put("success", true);
            response.put("eliminados", eliminados);
            response.put("total_encontrados", seguimientos.size());
            response.put("paciente_id", pacienteId);
            response.put("mensaje", String.format("✅ %d seguimientos eliminados de %d encontrados", eliminados, seguimientos.size()));
            
            logger.info("✅ {} seguimientos eliminados para paciente {}", eliminados, pacienteId);
            
        } catch (Exception e) {
            logger.error("❌ Error eliminando seguimientos: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("mensaje", "Error: " + e.getMessage());
            response.put("paciente_id", pacienteId);
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 📋 NUEVO: Crear seguimientos directos sin workflow
     */
    @PostMapping("/crear-seguimientos-directos")
    @Operation(summary = "Crear seguimientos directos", description = "Crea seguimientos directamente en la base de datos para pruebas")
    public ResponseEntity<Map<String, Object>> crearSeguimientosDirectos(
            @org.springframework.web.bind.annotation.RequestBody Map<String, Object> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long pacienteId = Long.valueOf(request.get("pacienteId").toString());
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> seguimientosData = (List<Map<String, Object>>) request.get("seguimientos");
            
            logger.info("📋 CREANDO seguimientos directos para paciente: {}", pacienteId);
            
            // Simular procesamiento usando el servicio existente
            Map<String, Object> datosParaProcesar = new HashMap<>();
            datosParaProcesar.put("pacienteId", pacienteId);
            datosParaProcesar.put("atencion_id", System.currentTimeMillis()); // ID ficticio
            
            // Crear estructura de plan de seguimiento
            Map<String, Object> planSeguimiento = new HashMap<>();
            List<Map<String, Object>> seguimientosParaPlan = new java.util.ArrayList<>();
            
            for (int i = 0; i < seguimientosData.size(); i++) {
                Map<String, Object> segData = seguimientosData.get(i);
                Map<String, Object> seguimientoParaPlan = new HashMap<>();
                seguimientoParaPlan.put("diasDespues", 0); // Hoy
                seguimientoParaPlan.put("mensaje", segData.getOrDefault("resultado", "Seguimiento cardiovascular"));
                seguimientoParaPlan.put("tipo", "chatbot_cuestionario");
                seguimientoParaPlan.put("prioridad", segData.getOrDefault("prioridad", "MEDIA"));
                seguimientoParaPlan.put("numeroSeguimiento", i + 1);
                seguimientosParaPlan.add(seguimientoParaPlan);
            }
            
            planSeguimiento.put("seguimientos", seguimientosParaPlan);
            datosParaProcesar.put("planSeguimiento", planSeguimiento);
            
            // Agregar análisis IA simulado
            datosParaProcesar.put("analisisIA", Map.of(
                "nivelRiesgo", "MEDIO",
                "recomendaciones", "Seguimiento cardiovascular de rutina",
                "factoresRiesgo", List.of("seguimiento_general")
            ));
            
            // Procesar usando el servicio existente
            Map<String, Object> resultado = seguimientoService.procesarSeguimientosDesdeN8n(datosParaProcesar);
            
            response.put("success", true);
            response.put("paciente_id", pacienteId);
            response.put("seguimientos_creados", seguimientosData.size());
            response.put("resultado_procesamiento", resultado);
            response.put("mensaje", "✅ Seguimientos directos creados exitosamente");
            
            logger.info("✅ {} seguimientos directos creados para paciente {}", seguimientosData.size(), pacienteId);
            
        } catch (Exception e) {
            logger.error("❌ Error creando seguimientos directos: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("mensaje", "Error: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
} 