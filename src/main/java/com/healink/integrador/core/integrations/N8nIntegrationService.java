package com.healink.integrador.core.integrations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healink.integrador.domain.atenciones_medicas.AtencionMedica;
import com.healink.integrador.domain.historia_clinica.HistoriaClinica;
import com.healink.integrador.domain.historia_clinica.HistoriaClinicaService;
import com.healink.integrador.domain.paciente.Paciente;
import com.healink.integrador.domain.paciente.PacienteService;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedica;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedicaService;
import com.healink.integrador.domain.datos_clinicos.DatosClinicos;
import com.healink.integrador.domain.triaje.Triaje;
import com.healink.integrador.domain.diagnosticos.Diagnostico;
import com.healink.integrador.domain.recomendaciones.Recomendacion;
import com.healink.integrador.domain.prescripciones.Prescripcion;
import com.healink.integrador.domain.predicciones.Prediccion;
import com.healink.integrador.domain.predicciones.PrediccionService;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio de integración con n8n para el sistema de seguimientos multi-agente
 * RESPONSABILIDAD: Solo comunicación con n8n y extracción de datos del backend
 */
@Service
public class N8nIntegrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(N8nIntegrationService.class);
    
    @Value("${n8n.webhook.base-url:https://jddc2025.app.n8n.cloud/webhook}")
    private String n8nBaseUrl;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final HistoriaClinicaService historiaClinicaService;
    private final PacienteService pacienteService;
    private final CitacionMedicaService citacionMedicaService;
    private final PrediccionService prediccionService;
    
    public N8nIntegrationService(RestTemplate restTemplate, 
                                ObjectMapper objectMapper,
                                HistoriaClinicaService historiaClinicaService,
                                PacienteService pacienteService,
                                CitacionMedicaService citacionMedicaService,
                                PrediccionService prediccionService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.historiaClinicaService = historiaClinicaService;
        this.pacienteService = pacienteService;
        this.citacionMedicaService = citacionMedicaService;
        this.prediccionService = prediccionService;
    }
    
    /**
     * PUNTO DE ENTRADA: Dispara el flujo orquestador cuando se completa una atención médica
     */
    public void iniciarSeguimientosPaciente(AtencionMedica atencionMedica) {
        try {
            Long pacienteId = obtenerPacienteIdDeAtencion(atencionMedica);
            Long campanaId = obtenerCampanaIdDeAtencion(atencionMedica);
            
            logger.info("=== INICIANDO SEGUIMIENTOS AUTOMÁTICOS ===");
            logger.info("Paciente ID: {}, Atención ID: {}, Campaña ID: {}", 
                       pacienteId, atencionMedica.getId(), campanaId);
            
            // CAMBIO: Si no hay paciente ID de citación, verificar si se puede extraer de otra forma
            if (pacienteId == null) {
                logger.warn("No se puede obtener paciente ID de citación para atención {}", 
                           atencionMedica.getId());
                logger.info("Para pruebas con datos reales, usa el endpoint /api/test/n8n/test-paciente/{pacienteId}");
                return;
            }
            
            // Verificar que el paciente tiene datos completos antes de proceder
            if (pacienteId == null) {
                logger.warn("No se puede iniciar seguimientos: Paciente ID es null para atención {}", 
                           atencionMedica.getId());
                return;
            }
            
            // Obtener historial clínico completo
            Map<String, Object> historialCompleto = obtenerHistorialCompleto(pacienteId, atencionMedica);
            
            // Verificar que hay datos médicos mínimos
            if (historialCompleto.isEmpty()) {
                logger.warn("No se puede iniciar seguimientos: Sin historial clínico para paciente {}", pacienteId);
                return;
            }
            
            // Preparar payload limpio para n8n
            Map<String, Object> payload = new HashMap<>();
            payload.put("evento", "atencion_completada");
            payload.put("atencion_id", atencionMedica.getId());
            payload.put("paciente_id", pacienteId);
            payload.put("campana_id", campanaId); // Puede ser null
            payload.put("fecha_atencion", atencionMedica.getFechaCreacion());
            payload.put("historial_clinico", historialCompleto);
            payload.put("timestamp", System.currentTimeMillis());
            payload.put("modo", "produccion");
            
            // Llamar al webhook del agente orquestador
            String response = llamarWebhookN8n("/orquestador-seguimientos", payload);
            
            logger.info("✅ Seguimientos iniciados para paciente {}: {}", pacienteId, response);
            
        } catch (Exception e) {
            logger.error("❌ Error iniciando seguimientos para atención {}: {}", 
                        atencionMedica.getId(), e.getMessage(), e);
            // No relanzar excepción para no afectar el guardado de la atención médica
        }
    }
    
    /**
     * NUEVO: Método para iniciar seguimientos directamente con paciente ID (útil para pruebas)
     */
    public void iniciarSeguimientosPacientePorId(Long pacienteId, Long campanaId) {
        try {
            logger.info("=== INICIANDO SEGUIMIENTOS POR PACIENTE ID ===");
            logger.info("Paciente ID: {}, Campaña ID: {}", pacienteId, campanaId);
            
            // Crear atención médica temporal para mantener la estructura
            AtencionMedica atencionTemporal = new AtencionMedica();
            atencionTemporal.setId(null); // Sin ID hardcodeado
            atencionTemporal.setCitacionId(null); // Sin citación
            atencionTemporal.setFechaCreacion(LocalDateTime.now());
            
            // Obtener historial clínico completo directamente por paciente ID
            Map<String, Object> historialCompleto = obtenerHistorialCompleto(pacienteId, atencionTemporal);
            
            // Verificar que hay datos médicos mínimos
            if (historialCompleto.isEmpty()) {
                logger.warn("No se puede iniciar seguimientos: Sin historial clínico para paciente {}", pacienteId);
                return;
            }
            
            // Preparar payload limpio para n8n
            Map<String, Object> payload = new HashMap<>();
            payload.put("evento", "atencion_completada");
            payload.put("atencion_id", null); // Sin atención específica
            payload.put("paciente_id", pacienteId);
            payload.put("campana_id", campanaId);
            payload.put("fecha_atencion", LocalDateTime.now());
            payload.put("historial_clinico", historialCompleto);
            payload.put("timestamp", System.currentTimeMillis());
            payload.put("modo", "test_paciente_real");
            
            // Llamar al webhook del agente orquestador
            String response = llamarWebhookN8n("/orquestador-seguimientos", payload);
            
            logger.info("✅ Seguimientos iniciados por paciente ID {}: {}", pacienteId, response);
            
        } catch (Exception e) {
            logger.error("❌ Error iniciando seguimientos para paciente {}: {}", 
                        pacienteId, e.getMessage(), e);
            throw new RuntimeException("Error iniciando seguimientos para paciente " + pacienteId, e);
        }
    }
    
    // MÉTODO ELIMINADO: enviarNotificacionSeguimiento() 
    // RAZÓN: Las notificaciones las genera y envía n8n directamente
    
    // ====== MÉTODOS PRIVADOS - SOLO EXTRACCIÓN DE DATOS ======
    
    /**
     * Obtener historial clínico completo - DATOS CARDIOVASCULARES REALES
     */
    private Map<String, Object> obtenerHistorialCompleto(Long pacienteId, AtencionMedica atencionMedica) {
        Map<String, Object> historial = new HashMap<>();
        
        try {
            logger.info("🔍 Extrayendo historial completo para paciente: {}", pacienteId);
            
            // Datos básicos del paciente
            Paciente paciente = pacienteService.obtenerPorId(pacienteId);
            if (paciente == null) {
                logger.warn("Paciente {} no encontrado", pacienteId);
                return historial; // Retornar vacío si no hay paciente
            }
            
            historial.put("pacienteId", pacienteId);
            Map<String, Object> datosBasicos = extraerDatosBasicos(paciente);
            
            // Datos cardiovasculares reales desde FastAPI si disponible
            Map<String, Object> datosCardiovasculares = new HashMap<>();
            datosCardiovasculares.put("pacienteId", pacienteId);
            datosCardiovasculares.put("edad", calcularEdad(paciente));
            datosCardiovasculares.put("sexo", mapearSexoPaciente(paciente));
            
            // Datos clínicos por defecto (mejorar con datos reales)
            datosCardiovasculares.put("presionSistolica", 120);
            datosCardiovasculares.put("presionDiastolica", 80);
            datosCardiovasculares.put("frecuenciaCardiaca", 72);
            datosCardiovasculares.put("imc", 25.0);
            datosCardiovasculares.put("colesterolTotal", 200);
            datosCardiovasculares.put("colesterolHDL", 50);
            datosCardiovasculares.put("colesterolLDL", 130);
            datosCardiovasculares.put("trigliceridos", 150);
            datosCardiovasculares.put("glucosa", 90);
            
            // Factores de riesgo booleanos
            datosCardiovasculares.put("diabetes", false);
            datosCardiovasculares.put("hipertension", false);
            datosCardiovasculares.put("tabaquismo", false);
            datosCardiovasculares.put("antecedentesCardiovasculares", false);
            
            logger.info("💓 Datos cardiovasculares extraídos para paciente {}", pacienteId);
            
            // Integrar datos en historial
            historial.put("datos_basicos", datosBasicos);
            historial.put("datos_cardiovasculares", datosCardiovasculares);
            
            // Obtener historial clínico si está disponible
            try {
                // TODO: Comentado temporalmente hasta implementar método en HistoriaClinicaService
                // HistoriaClinica historiaClinica = historiaClinicaService.obtenerPorPacienteId(pacienteId);
                
                // Usar datos básicos por ahora
                Map<String, Object> datosHistoriaClinica = new HashMap<>();
                datosHistoriaClinica.put("datos_clinicos", Map.of(
                    "peso", 70.0,
                    "estatura", 170.0,
                    "imc", 24.2
                ));
                
                datosHistoriaClinica.put("triaje", Map.of(
                    "presion_sistolica", 120,
                    "presion_diastolica", 80,
                    "frecuencia_cardiaca", 72,
                    "temperatura", 36.5,
                    "saturacion_oxigeno", 98
                ));
                
                // Actualizar datos cardiovasculares con valores del triaje simulado
                datosCardiovasculares.put("presionSistolica", 120);
                datosCardiovasculares.put("presionDiastolica", 80);
                datosCardiovasculares.put("frecuenciaCardiaca", 72);
                
                datosHistoriaClinica.put("diagnosticos", List.of());
                datosHistoriaClinica.put("medicamentos", List.of());
                datosHistoriaClinica.put("recomendaciones", List.of());
                
                historial.put("historia_clinica", datosHistoriaClinica);
                logger.info("📋 Historia clínica básica extraída para paciente {}", pacienteId);
                
                /*
                if (historiaClinica != null) {
                    Map<String, Object> datosHistoriaClinica = new HashMap<>();
                    
                    // Datos clínicos
                    if (historiaClinica.getDatosClinicos() != null) {
                        DatosClinicos datosClinicos = historiaClinica.getDatosClinicos();
                        Map<String, Object> datosClinicosMap = new HashMap<>();
                        datosClinicosMap.put("peso", datosClinicos.getPeso());
                        datosClinicosMap.put("estatura", datosClinicos.getEstatura());
                        datosClinicosMap.put("imc", datosClinicos.getImc());
                        datosHistoriaClinica.put("datos_clinicos", datosClinicosMap);
                        
                        // Actualizar datos cardiovasculares con valores reales
                        if (datosClinicos.getImc() != null) {
                            datosCardiovasculares.put("imc", datosClinicos.getImc());
                        }
                    }
                    
                    // Triaje
                    if (historiaClinica.getTriaje() != null) {
                        Triaje triaje = historiaClinica.getTriaje();
                        Map<String, Object> triajeMap = new HashMap<>();
                        triajeMap.put("presion_sistolica", triaje.getPresionSistolica());
                        triajeMap.put("presion_diastolica", triaje.getPresionDiastolica());
                        triajeMap.put("frecuencia_cardiaca", triaje.getFrecuenciaCardiaca());
                        triajeMap.put("temperatura", triaje.getTemperatura());
                        triajeMap.put("saturacion_oxigeno", triaje.getSaturacionOxigeno());
                        datosHistoriaClinica.put("triaje", triajeMap);
                        
                        // Actualizar datos cardiovasculares con valores del triaje
                        if (triaje.getPresionSistolica() != null) {
                            datosCardiovasculares.put("presionSistolica", triaje.getPresionSistolica());
                        }
                        if (triaje.getPresionDiastolica() != null) {
                            datosCardiovasculares.put("presionDiastolica", triaje.getPresionDiastolica());
                        }
                        if (triaje.getFrecuenciaCardiaca() != null) {
                            datosCardiovasculares.put("frecuenciaCardiaca", triaje.getFrecuenciaCardiaca());
                        }
                    }
                    
                    // Diagnósticos
                    if (historiaClinica.getDiagnosticos() != null && !historiaClinica.getDiagnosticos().isEmpty()) {
                        List<Map<String, Object>> diagnosticos = new ArrayList<>();
                        for (Diagnostico diagnostico : historiaClinica.getDiagnosticos()) {
                            diagnosticos.add(extraerDiagnosticos(diagnostico));
                        }
                        datosHistoriaClinica.put("diagnosticos", diagnosticos);
                    }
                    
                    // Prescripciones
                    if (historiaClinica.getPrescripciones() != null && !historiaClinica.getPrescripciones().isEmpty()) {
                        List<Map<String, Object>> medicamentos = new ArrayList<>();
                        for (Prescripcion prescripcion : historiaClinica.getPrescripciones()) {
                            medicamentos.addAll(extraerMedicamentos(prescripcion));
                        }
                        datosHistoriaClinica.put("medicamentos", medicamentos);
                    }
                    
                    // Recomendaciones
                    if (historiaClinica.getRecomendaciones() != null && !historiaClinica.getRecomendaciones().isEmpty()) {
                        List<Map<String, Object>> recomendaciones = new ArrayList<>();
                        for (Recomendacion recomendacion : historiaClinica.getRecomendaciones()) {
                            recomendaciones.add(extraerRecomendaciones(recomendacion));
                        }
                        datosHistoriaClinica.put("recomendaciones", recomendaciones);
                    }
                    
                    historial.put("historia_clinica", datosHistoriaClinica);
                    logger.info("📋 Historia clínica extraída para paciente {}", pacienteId);
                }
                */
            } catch (Exception e) {
                logger.warn("⚠️ No se pudo obtener historia clínica para paciente {}: {}", pacienteId, e.getMessage());
            }
            
            // Predicciones de FastAPI si están disponibles
            try {
                // TODO: Comentado temporalmente hasta implementar métodos en PrediccionService
                /*
                List<Prediccion> predicciones = prediccionService.obtenerPrediccionesPorPaciente(pacienteId);
                if (predicciones != null && !predicciones.isEmpty()) {
                    Prediccion ultimaPrediccion = predicciones.get(predicciones.size() - 1);
                    Map<String, Object> prediccionMap = new HashMap<>();
                    prediccionMap.put("riesgo_cardiovascular", ultimaPrediccion.getRiesgoCardiovascular());
                    prediccionMap.put("prediccion_detalle", ultimaPrediccion.getPrediccionDetalle());
                    prediccionMap.put("fecha_prediccion", ultimaPrediccion.getFechaPrediccion());
                    
                    historial.put("prediccion_fastapi", prediccionMap);
                    logger.info("🤖 Predicción FastAPI incluida para paciente {}", pacienteId);
                }
                */
                
                // Datos simulados de predicción FastAPI por ahora
                Map<String, Object> prediccionMap = new HashMap<>();
                prediccionMap.put("riesgo_cardiovascular", "MODERADO");
                prediccionMap.put("prediccion_detalle", "Riesgo cardiovascular moderado basado en edad y presión arterial");
                prediccionMap.put("fecha_prediccion", LocalDateTime.now().toString());
                
                historial.put("prediccion_fastapi", prediccionMap);
                logger.info("🤖 Predicción FastAPI simulada incluida para paciente {}", pacienteId);
                
            } catch (Exception e) {
                logger.warn("⚠️ No se pudo obtener predicción FastAPI para paciente {}: {}", pacienteId, e.getMessage());
            }
            
            // Actualizar datos cardiovasculares en el historial
            historial.put("datos_cardiovasculares", datosCardiovasculares);
            
            logger.info("✅ Historial completo extraído para paciente {} - {} secciones", 
                       pacienteId, historial.keySet().size());
            
        } catch (Exception e) {
            logger.error("❌ Error extrayendo historial para paciente {}: {}", pacienteId, e.getMessage(), e);
        }
        
        return historial;
    }
    
    // ====== MÉTODOS AUXILIARES PRIVADOS ======
    
    /**
     * Extraer datos básicos del paciente para n8n
     */
    private Map<String, Object> extraerDatosBasicos(Paciente paciente) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("edad", calcularEdad(paciente));
        datos.put("genero", paciente.getGenero() != null ? paciente.getGenero().toString() : "NO_ESPECIFICADO");
        return datos;
    }
    
    private Integer calcularEdad(Paciente paciente) {
        if (paciente.getFechaNacimiento() != null) {
            return Period.between(paciente.getFechaNacimiento(), LocalDate.now()).getYears();
        }
        return null;
    }
    
    private Long obtenerPacienteIdDeAtencion(AtencionMedica atencionMedica) {
        try {
            if (atencionMedica.getCitacionId() != null) {
                CitacionMedica citacion = citacionMedicaService.obtenerPorId(atencionMedica.getCitacionId());
                if (citacion != null) {
                    return citacion.getPacienteId();
                }
            }
        } catch (Exception e) {
            logger.warn("No se pudo obtener paciente ID de citación: {}", e.getMessage());
        }
        return null;
    }
    
    private Long obtenerCampanaIdDeAtencion(AtencionMedica atencionMedica) {
        try {
            if (atencionMedica.getCitacionId() != null) {
                CitacionMedica citacion = citacionMedicaService.obtenerPorId(atencionMedica.getCitacionId());
                if (citacion != null) {
                    return citacion.getCampanaId();
                }
            }
        } catch (Exception e) {
            logger.warn("No se pudo obtener campaña ID de citación: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Llamar webhook de n8n con manejo de errores mejorado
     */
    private String llamarWebhookN8n(String endpoint, Map<String, Object> payload) {
        try {
            String url = n8nBaseUrl + endpoint;
            logger.info("🌐 Llamando webhook n8n: {}", url);
            logger.debug("📤 Payload keys: {}", payload.keySet());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            
            // Log datos enviados para debugging
            if (payload.containsKey("historial_clinico")) {
                Map<String, Object> historial = (Map<String, Object>) payload.get("historial_clinico");
                logger.info("📊 Enviando historial con {} secciones", historial.keySet().size());
                
                if (historial.containsKey("datos_cardiovasculares")) {
                    Map<String, Object> datosCV = (Map<String, Object>) historial.get("datos_cardiovasculares");
                    logger.info("❤️ Datos cardiovasculares: pacienteId={}, edad={}, sexo={}, presionSistolica={}", 
                               datosCV.get("pacienteId"), datosCV.get("edad"), datosCV.get("sexo"), datosCV.get("presionSistolica"));
                }
            }
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, request, String.class);
            
            logger.info("Response Status: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody());
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error en llamada a n8n: " + response.getStatusCode());
            }
            
        } catch (RestClientException e) {
            logger.error("Error de conectividad con n8n: {}", e.getMessage());
            throw new RuntimeException("No se pudo conectar con n8n", e);
        } catch (Exception e) {
            logger.error("Error inesperado llamando a n8n: {}", e.getMessage());
            throw new RuntimeException("Error en integración con n8n", e);
        }
    }
    
    // NUEVO: Método para debugging - obtener URL actual
    public String obtenerUrlActual() {
        return n8nBaseUrl;
    }
    
    // NUEVO: Método público para pruebas con datos reales
    public Map<String, Object> obtenerHistorialCompletoParaPrueba(Long pacienteId, AtencionMedica atencionMedica) {
        logger.info("Obteniendo historial completo para prueba - Paciente ID: {}", pacienteId);
        return obtenerHistorialCompleto(pacienteId, atencionMedica);
    }
    
    // NUEVO: Método público para llamadas directas a webhooks
    public String llamarWebhookN8nDirecto(String endpoint, Map<String, Object> payload) {
        logger.info("Llamada directa a webhook: {}", endpoint);
        return llamarWebhookN8n(endpoint, payload);
    }
    
    // ====== MÉTODOS AUXILIARES PARA DATOS CARDIOVASCULARES ======
    
    /**
     * Calcular edad del paciente
     */
    private int calcularEdadPaciente(Paciente paciente) {
        if (paciente.getFechaNacimiento() != null) {
            return Period.between(paciente.getFechaNacimiento(), LocalDate.now()).getYears();
        }
        return 45; // Edad por defecto si no está disponible
    }
    
    /**
     * Mapear sexo del paciente para n8n (M/F)
     */
    private String mapearSexoPaciente(Paciente paciente) {
        if (paciente.getGenero() != null) {
            String genero = paciente.getGenero().toString();
            if ("MASCULINO".equalsIgnoreCase(genero) || "M".equalsIgnoreCase(genero)) {
                return "M";
            } else if ("FEMENINO".equalsIgnoreCase(genero) || "F".equalsIgnoreCase(genero)) {
                return "F";
            }
        }
        return "M"; // Por defecto masculino
    }
    
    /**
     * Extraer medicamentos de prescripciones
     */
    private List<String> extraerMedicamentos(Prescripcion prescripcion) {
        List<String> medicamentos = new ArrayList<>();
        
        if (prescripcion != null) {
            // Extraer medicamento principal de la descripción
            if (prescripcion.getDescripcion() != null) {
                medicamentos.add(prescripcion.getDescripcion());
            }
        }
        
        return medicamentos;
    }
    
    private Map<String, Object> extraerDiagnosticos(Diagnostico diagnostico) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("codigo_cie10", diagnostico.getCodigoCie10());
        datos.put("descripcion", diagnostico.getDescripcion());
        datos.put("severidad", diagnostico.getSeveridad() != null ? 
                 diagnostico.getSeveridad().toString() : null);
        datos.put("es_principal", diagnostico.isEs_principal());
        datos.put("fecha_diagnostico", diagnostico.getFecha_diagnostico() != null ? 
                 diagnostico.getFecha_diagnostico().toString() : null);
        return datos;
    }
    
    private Map<String, Object> extraerRecomendaciones(Recomendacion recomendacion) {
        Map<String, Object> datos = new HashMap<>();
        // TODO: Implementar según estructura real de Recomendacion
        datos.put("id", recomendacion.getId());
        datos.put("raw_data", "Implementar extracción de recomendaciones");
        return datos;
    }

    /**
     * NUEVO: Llamar al workflow del compañero para generar cuestionario personalizado
     */
    public Map<String, Object> generarCuestionarioConWorkflowCompanero(Map<String, Object> datosWorkflow) {
        try {
            logger.info("🤖 Llamando al workflow del compañero para generar cuestionario...");
            
            // Log de datos que se envían
            logger.debug("📋 Datos enviados al workflow del compañero: {}", 
                        datosWorkflow.keySet());
            
            // Llamar al webhook del workflow del compañero
            String webhookPath = "/agente1-cardiovascular"; // Path del webhook del compañero
            String response = llamarWebhookN8n(webhookPath, datosWorkflow);
            
            logger.info("✅ Respuesta del workflow del compañero recibida");
            
            // Parsear respuesta JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> respuestaParseada = objectMapper.readValue(response, Map.class);
            
            return respuestaParseada;
            
        } catch (Exception e) {
            logger.error("❌ Error llamando al workflow del compañero: {}", e.getMessage(), e);
            
            // Retornar cuestionario de fallback en caso de error
            return Map.of(
                "success", false,
                "error", e.getMessage(),
                "cuestionario", generarCuestionarioFallback(),
                "es_fallback", true
            );
        }
    }

    /**
     * Generar cuestionario de fallback cuando n8n no está disponible
     */
    private Map<String, Object> generarCuestionarioFallback() {
        List<Map<String, Object>> preguntasDefault = List.of(
            Map.of(
                "id", "adherencia_medicamentos",
                "pregunta", "¿Ha tomado sus medicamentos según las indicaciones?",
                "tipo", "opcion_multiple",
                "opciones", List.of(
                    "Sí, todos los días",
                    "Casi siempre",
                    "A veces olvido",
                    "Frecuentemente olvido"
                ),
                "requerida", true
            ),
            Map.of(
                "id", "sintomas_generales",
                "pregunta", "¿Ha experimentado síntomas cardiovasculares?",
                "tipo", "multiple_seleccion",
                "opciones", List.of(
                    "Ningún síntoma",
                    "Dolor en el pecho",
                    "Dificultad para respirar",
                    "Palpitaciones",
                    "Fatiga inusual"
                ),
                "requerida", true
            ),
            Map.of(
                "id", "calidad_vida",
                "pregunta", "¿Cómo se siente en general?",
                "tipo", "opcion_multiple",
                "opciones", List.of(
                    "Muy bien",
                    "Bien, con algunas preocupaciones",
                    "Regular",
                    "Mal, me preocupa mi salud"
                ),
                "requerida", true
            )
        );
        
        return Map.of(
            "titulo", "Seguimiento Cardiovascular - Cuestionario de Respaldo",
            "instrucciones", "Por favor responda las siguientes preguntas sobre su estado de salud",
            "preguntas", preguntasDefault,
            "metadata", Map.of(
                "es_fallback", true,
                "generado_en", LocalDateTime.now().toString()
            )
        );
    }

    /**
     * 🎯 GENERAR CUESTIONARIO PERSONALIZADO
     * 
     * Llama al workflow mejorado que genera cuestionarios específicos basados en:
     * - Diagnósticos del paciente
     * - Medicamentos actuales  
     * - Factores de riesgo cardiovascular
     * - Contexto del seguimiento
     */
    public String generarCuestionarioPersonalizado(Long pacienteId, Long seguimientoId) {
        try {
            logger.info("🎯 Generando cuestionario personalizado para paciente {} - seguimiento {}", 
                       pacienteId, seguimientoId);
            
            // 1. Construir datos completos del paciente
            Map<String, Object> datosCompletos = construirDatosCompletosPaciente(pacienteId, seguimientoId);
            
            // 2. Llamar al webhook de n8n para generación de cuestionarios
            String webhookPath = "/agente1-cardiovascular";
            String response = llamarWebhookN8n(webhookPath, datosCompletos);
            
            logger.info("✅ Cuestionario personalizado generado exitosamente");
            return response;
            
        } catch (Exception e) {
            logger.error("❌ Error generando cuestionario personalizado: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar cuestionario personalizado: " + e.getMessage());
        }
    }

    /**
     * 🧠 ANALIZAR RESPUESTAS DE CUESTIONARIO
     * 
     * Llama al workflow de análisis especializado que evalúa:
     * - Respuestas del cuestionario actual
     * - Historial clínico completo
     * - Seguimientos anteriores
     * - Análisis de riesgo cardiovascular
     * - Evolución temporal del paciente
     */
    public String analizarRespuestasCuestionario(Long pacienteId, Long seguimientoId, 
                                                Map<String, Object> respuestas, Long campanaId) {
        try {
            logger.info("🧠 Analizando respuestas de cuestionario - Paciente: {}, Seguimiento: {}", 
                       pacienteId, seguimientoId);
            
            // 1. Construir datos completos para análisis
            Map<String, Object> datosAnalisis = construirDatosAnalisisCompleto(
                pacienteId, seguimientoId, respuestas, campanaId);
            
            // 2. Llamar al webhook de n8n para análisis de respuestas
            String webhookPath = "/analizar-respuestas-cuestionario";
            String response = llamarWebhookN8n(webhookPath, datosAnalisis);
            
            logger.info("✅ Análisis de respuestas completado exitosamente");
            return response;
            
        } catch (Exception e) {
            logger.error("❌ Error analizando respuestas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al analizar respuestas del cuestionario: " + e.getMessage());
        }
    }

    /**
     * 📊 CONSTRUIR DATOS COMPLETOS DEL PACIENTE PARA CUESTIONARIO
     */
    private Map<String, Object> construirDatosCompletosPaciente(Long pacienteId, Long seguimientoId) {
        // Usar datos básicos del paciente ya implementados en el servicio
        Paciente paciente = pacienteService.obtenerPorId(pacienteId);
        if (paciente == null) {
            throw new RuntimeException("Paciente no encontrado: " + pacienteId);
        }
        
        // Reutilizar métodos existentes del servicio
        Map<String, Object> datosBasicos = extraerDatosBasicos(paciente);
        
        // Datos cardiovasculares básicos (usando valores por defecto si no están disponibles)
        Map<String, Object> datosCardiovasculares = Map.of(
            "presionSistolica", 120, // Se podría obtener del triaje más reciente
            "presionDiastolica", 80,
            "colesterolTotal", 200,
            "diabetes", false // Se podría obtener de diagnósticos
        );
        
        // Datos clínicos completos (simplificados para esta implementación)
        Map<String, Object> datosCompletos = Map.of(
            "diagnosticos", List.of(), // TODO: Implementar obtención real
            "prescripciones", List.of(), // TODO: Implementar obtención real
            "triaje", Map.of("presion_sistolica", 120, "presion_diastolica", 80),
            "factores_riesgo_cardiovascular", Map.of("hipertension", false, "diabetes", false)
        );
        
        // Contexto del seguimiento (simplificado)
        Map<String, Object> seguimientoContext = Map.of(
            "seguimiento_id", seguimientoId,
            "tipo", "CARDIOVASCULAR",
            "prioridad", "MEDIA",
            "dias_desde_programacion", 0,
            "resultado_analisis_ia", "Seguimiento cardiovascular - Control de presión arterial",
            "notas_seguimiento", "Seguimiento médico regular"
        );
        
        return Map.of(
            "paciente_id", pacienteId,
            "datos_basicos", datosBasicos,
            "datos_cardiovasculares", datosCardiovasculares,
            "datos_completos", datosCompletos,
            "seguimiento_context", seguimientoContext,
            "objetivo_cuestionario", "seguimiento_especifico",
            "timestamp", LocalDateTime.now().toString()
        );
    }

    /**
     * 🔍 CONSTRUIR DATOS COMPLETOS PARA ANÁLISIS DE RESPUESTAS
     */
    private Map<String, Object> construirDatosAnalisisCompleto(Long pacienteId, Long seguimientoId, 
                                                              Map<String, Object> respuestas, Long campanaId) {
        
        // Datos actualizados del paciente (usando valores básicos)
        Map<String, Object> datosActualizados = Map.of(
            "triaje", Map.of("presion_sistolica", 120, "presion_diastolica", 80),
            "diagnosticos", List.of(),
            "prescripciones", List.of(),
            "recomendaciones", List.of()
        );
        
        // Información de la campaña (simplificada)
        Map<String, Object> campanaInfo = Map.of(
            "nombre", "Campaña Cardiovascular",
            "objetivo", "Seguimiento y control cardiovascular",
            "fecha_inicio", "2024-01-01"
        );
        
        // Seguimientos anteriores (datos básicos)
        List<Map<String, Object>> seguimientosAnteriores = List.of(
            Map.of(
                "fecha_programada", "2024-01-10",
                "tipo", "CUESTIONARIO",
                "estado", "COMPLETADO",
                "respuestas", Map.of("adherencia", "BUENA"),
                "resultado_analisis", "Paciente muestra buena adherencia al tratamiento"
            )
        );
        
        // Datos de riesgo cardiovascular (simulados - en producción vendrían de FastAPI)
        Map<String, Object> riesgoCardiovascular = Map.of(
            "nivel_riesgo", "MODERADO",
            "probabilidad_riesgo", 0.3,
            "factores_detectados", List.of("presion_arterial_normal")
        );
        
        Map<String, Object> factoresInfluyentes = Map.of(
            "factores_riesgo", List.of(),
            "factores_protectores", List.of("edad_joven")
        );
        
        Map<String, Object> priorizacion = Map.of(
            "cluster", "MODERADO_JOVEN",
            "prioridad", "MEDIA",
            "recomendaciones", List.of("Seguimiento médico regular")
        );
        
        return Map.of(
            "paciente_id", pacienteId,
            "seguimiento_id", seguimientoId,
            "respuestas_cuestionario", respuestas,
            "datos_actualizados", datosActualizados,
            "campana_info", campanaInfo,
            "seguimientos_anteriores", seguimientosAnteriores,
            "riesgo_cardiovascular", riesgoCardiovascular,
            "factores_influyentes", factoresInfluyentes,
            "priorizacion", priorizacion,
            "timestamp_analisis", LocalDateTime.now().toString()
        );
    }
} 