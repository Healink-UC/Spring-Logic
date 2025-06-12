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

import com.healink.integrador.domain.citaciones_medicas.CitacionMedica;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedicaService;
import com.healink.integrador.domain.citaciones_medicas.EstadoCitacion;
import com.healink.integrador.domain.seguimientos.Seguimiento;
import com.healink.integrador.domain.seguimientos.SeguimientoService;
import com.healink.integrador.domain.seguimientos.SeguimientoDTO;
import com.healink.integrador.domain.seguimientos.SeguimientoMapper;
import com.healink.integrador.core.integrations.N8nIntegrationService;
import com.healink.integrador.domain.paciente.Paciente;
import com.healink.integrador.domain.paciente.PacienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test/paso3")
@Tag(name = "Test Paso 3", description = "Endpoints de prueba para verificar funcionalidad del Paso 3")
public class TestPaso3Controller {

    private static final Logger logger = LoggerFactory.getLogger(TestPaso3Controller.class);
    
    private final CitacionMedicaService citacionMedicaService;
    private final SeguimientoService seguimientoService;
    private final SeguimientoMapper seguimientoMapper;
    private final N8nIntegrationService n8nIntegrationService;
    private final PacienteService pacienteService;

    public TestPaso3Controller(
            CitacionMedicaService citacionMedicaService,
            SeguimientoService seguimientoService,
            SeguimientoMapper seguimientoMapper,
            N8nIntegrationService n8nIntegrationService,
            PacienteService pacienteService) {
        
        this.citacionMedicaService = citacionMedicaService;
        this.seguimientoService = seguimientoService;
        this.seguimientoMapper = seguimientoMapper;
        this.n8nIntegrationService = n8nIntegrationService;
        this.pacienteService = pacienteService;
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
            
            // Crear citación médica simulada como completada
            CitacionMedica citacionSimulada = new CitacionMedica();
            citacionSimulada.setHoraProgramada(LocalDateTime.now().minusMinutes(30));
            citacionSimulada.setHoraAtencion(LocalDateTime.now());
            citacionSimulada.setDuracionEstimada(30);
            citacionSimulada.setEstado(EstadoCitacion.ATENDIDA);
            
            // Usar el método directo para iniciar seguimientos por paciente ID
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
            // NOTA: Aquí removimos referencias a atenciones médicas
            long totalSeguimientos = seguimientoService.obtenerSeguimientosPorPaciente(1L).size();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("sistema_n8n", Map.of(
                "url", urlN8n,
                "status", "OPERATIVO"
            ));
            response.put("base_datos", Map.of(
                "total_seguimientos", totalSeguimientos,
                "status", "CONECTADO"
            ));
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error verificando sistema: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
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
     * 🏗️ NUEVO: Crear citación temporal para pruebas
     */
    @PostMapping("/crear-atencion-temporal")
    @Operation(summary = "Crear citación temporal", description = "Crea una citación médica temporal para pruebas")
    public ResponseEntity<Map<String, Object>> crearAtencionTemporal(
            @org.springframework.web.bind.annotation.RequestBody Map<String, Object> request) {
        
        try {
            logger.info("🧪 CREANDO citación temporal para pruebas: {}", request);
            
            Long pacienteId = Long.valueOf(request.get("paciente_id").toString());
            Integer duracion = Integer.valueOf(request.getOrDefault("duracion", 30).toString());
            
            // Crear citación médica temporal
            CitacionMedica citacionTemporal = new CitacionMedica();
            citacionTemporal.setHoraProgramada(LocalDateTime.now().minusMinutes(duracion));
            citacionTemporal.setHoraAtencion(LocalDateTime.now());
            citacionTemporal.setDuracionEstimada(duracion);
            citacionTemporal.setEstado(EstadoCitacion.ATENDIDA);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Citación temporal creada exitosamente");
            response.put("citacion_temporal", Map.of(
                "hora_programada", citacionTemporal.getHoraProgramada(),
                "hora_atencion", citacionTemporal.getHoraAtencion(),
                "duracion_estimada", citacionTemporal.getDuracionEstimada(),
                "estado", citacionTemporal.getEstado()
            ));
            response.put("timestamp", LocalDateTime.now());
            
            // Iniciar seguimientos automáticamente
            n8nIntegrationService.iniciarSeguimientosPacientePorId(pacienteId, null);
            response.put("seguimientos_iniciados", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error creando citación temporal: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
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

    /**
     * 🎯 NUEVO: Probar flujo completo de seguimientos desde citación atendida
     */
    @PostMapping("/test-flujo-seguimientos-citacion/{pacienteId}")
    public ResponseEntity<Map<String, Object>> probarFlujoSeguimientosCitacion(@PathVariable Long pacienteId) {
        logger.info("🧪 PROBANDO FLUJO COMPLETO DE SEGUIMIENTOS: Paciente {}", pacienteId);
        
        try {
            // 1. Crear citación temporal
            CitacionMedica citacionTemp = new CitacionMedica();
            citacionTemp.setPacienteId(pacienteId);
            citacionTemp.setCampanaId(1L);
            citacionTemp.setMedicoId(1L);
            citacionTemp.setHoraProgramada(LocalDateTime.now());
            citacionTemp.setEstado(EstadoCitacion.AGENDADA);
            citacionTemp.setNotas("Prueba flujo seguimientos automáticos");
            
            CitacionMedica citacionGuardada = citacionMedicaService.guardar(citacionTemp);
            logger.info("📋 Citación creada: {}", citacionGuardada.getId());
            
            // 2. Marcar como atendida (esto debe disparar el evento y llamar a n8n)
            CitacionMedica citacionAtendida = citacionMedicaService.marcarComoAtendida(citacionGuardada.getId());
            logger.info("✅ Citación {} marcada como ATENDIDA", citacionAtendida.getId());
            
            // 3. Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Flujo de seguimientos ejecutado desde citación");
            response.put("paciente_id", pacienteId);
            response.put("citacion_id", citacionAtendida.getId());
            response.put("estado_citacion", citacionAtendida.getEstado());
            response.put("hora_atencion", citacionAtendida.getHoraAtencion());
            response.put("url_webhook_n8n", n8nIntegrationService.obtenerUrlActual());
            response.put("seguimientos_activados", true);
            response.put("timestamp", LocalDateTime.now());
            response.put("nota", "Revisa los logs para confirmar llamada a n8n");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error en flujo de seguimientos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage(),
                "paciente_id", pacienteId
            ));
        }
    }

    /**
     * 🔧 NUEVO: Endpoint directo para probar el webhook de n8n
     */
    @PostMapping("/test-webhook-n8n-directo/{pacienteId}")
    public ResponseEntity<Map<String, Object>> probarWebhookN8nDirecto(@PathVariable Long pacienteId) {
        logger.info("🌐 PROBANDO WEBHOOK N8N DIRECTO: Paciente {}", pacienteId);
        
        try {
            // Obtener datos del paciente
            Paciente paciente = pacienteService.obtenerPorId(pacienteId);
            if (paciente == null) {
                throw new RuntimeException("Paciente no encontrado: " + pacienteId);
            }
            
            // Crear payload de prueba directo
            Map<String, Object> datosCV = new HashMap<>();
            datosCV.put("pacienteId", pacienteId);
            //datosCV.put("edad", calcularEdad(paciente));
            //datosCV.put("sexo", obtenerSexo(paciente));
            datosCV.put("presionSistolica", 130);
            datosCV.put("presionDiastolica", 85);
            datosCV.put("frecuenciaCardiaca", 75);
            datosCV.put("colesterolTotal", 210);
            datosCV.put("colesterolHDL", 45);
            datosCV.put("colesterolLDL", 140);
            datosCV.put("trigliceridos", 160);
            datosCV.put("glucosa", 95);
            datosCV.put("tabaquismo", false);
            datosCV.put("hipertension", true);
            datosCV.put("diabetes", false);
            datosCV.put("antecedentesCardiovasculares", false);
            
            Map<String, Object> historialClinico = new HashMap<>();
            historialClinico.put("datos_cardiovasculares", datosCV);
            historialClinico.put("datos_basicos", Map.of(
                //"edad", calcularEdad(paciente),
                //"genero", obtenerSexo(paciente)
            ));
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("evento", "citacion_atendida");
            payload.put("paciente_id", pacienteId);
            payload.put("citacion_id", 999L); // ID temporal
            payload.put("campana_id", 1L);
            payload.put("fecha_atencion", LocalDateTime.now());
            payload.put("historial_clinico", historialClinico);
            payload.put("timestamp", System.currentTimeMillis());
            payload.put("modo", "test_directo");
            
            // Llamar directamente al webhook
            String response = n8nIntegrationService.llamarWebhookN8nDirecto("/orquestador-seguimientos", payload);
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("success", true);
            resultado.put("message", "Webhook n8n llamado directamente");
            resultado.put("paciente_id", pacienteId);
            resultado.put("url_webhook", n8nIntegrationService.obtenerUrlActual() + "/orquestador-seguimientos");
            resultado.put("payload_enviado", payload);
            resultado.put("respuesta_n8n", response);
            resultado.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            logger.error("❌ Error probando webhook n8n directo: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage(),
                "paciente_id", pacienteId,
                "url_webhook", n8nIntegrationService.obtenerUrlActual()
            ));
        }
    }
} 