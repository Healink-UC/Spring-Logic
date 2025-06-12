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
import com.healink.integrador.domain.citaciones_medicas.CitacionMedica;
import com.healink.integrador.domain.historia_clinica.HistoriaClinica;
import com.healink.integrador.domain.historia_clinica.HistoriaClinicaService;
import com.healink.integrador.domain.paciente.Paciente;
import com.healink.integrador.domain.paciente.PacienteService;
import com.healink.integrador.domain.citaciones_medicas.CitacionAtendidaEvent;
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
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

/**
 * Servicio de integración con n8n para el sistema de seguimientos multi-agente
 * RESPONSABILIDAD: Solo comunicación con n8n y extracción de datos del backend
 * NUEVA LÓGICA: Se activa cuando una citación médica cambia a estado ATENDIDA
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
    private final PrediccionService prediccionService;
    
    public N8nIntegrationService(RestTemplate restTemplate, 
                                ObjectMapper objectMapper,
                                HistoriaClinicaService historiaClinicaService,
                                PacienteService pacienteService,
                                PrediccionService prediccionService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.historiaClinicaService = historiaClinicaService;
        this.pacienteService = pacienteService;
        this.prediccionService = prediccionService;
    }
    
    /**
     * 🎯 EVENTO: Listener para citaciones atendidas
     */
    @EventListener
    public void manejarCitacionAtendida(CitacionAtendidaEvent evento) {
        logger.info("🎯 EVENTO RECIBIDO: Citación atendida {}", evento.getCitacionMedica().getId());
        try {
            iniciarSeguimientosPaciente(evento.getCitacionMedica());
        } catch (Exception e) {
            logger.error("❌ Error procesando evento de citación atendida: {}", e.getMessage(), e);
        }
    }
    
    /**
     * PUNTO DE ENTRADA: Dispara el flujo orquestador cuando una citación médica cambia a ATENDIDA
     */
    public void iniciarSeguimientosPaciente(CitacionMedica citacionMedica) {
        try {
            Long pacienteId = obtenerPacienteIdDeCitacion(citacionMedica);
            Long campanaId = obtenerCampanaIdDeCitacion(citacionMedica);
            
            logger.info("=== INICIANDO SEGUIMIENTOS AUTOMÁTICOS ===");
            logger.info("Paciente ID: {}, Citación ID: {}, Campaña ID: {}", 
                       pacienteId, citacionMedica.getId(), campanaId);
            
            // Verificar que el paciente ID es válido
            if (pacienteId == null) {
                logger.warn("No se puede obtener paciente ID de citación para citación {}", 
                           citacionMedica.getId());
                logger.info("Para pruebas con datos reales, usa el endpoint /api/test/n8n/test-paciente/{pacienteId}");
                return;
            }
            
            // Obtener historial clínico completo
            Map<String, Object> historialCompleto = obtenerHistorialCompleto(pacienteId, citacionMedica);
            
            // Verificar que hay datos médicos mínimos
            if (historialCompleto.isEmpty()) {
                logger.warn("No se puede iniciar seguimientos: Sin historial clínico para paciente {}", pacienteId);
                return;
            }
            
            // Preparar payload limpio para n8n
            Map<String, Object> payload = new HashMap<>();
            payload.put("evento", "citacion_atendida");
            payload.put("citacion_id", citacionMedica.getId());
            payload.put("paciente_id", pacienteId);
            payload.put("campana_id", campanaId); // Puede ser null
            payload.put("fecha_atencion", citacionMedica.getHoraAtencion() != null ? citacionMedica.getHoraAtencion() : citacionMedica.getFechaCreacion());
            payload.put("historial_clinico", historialCompleto);
            payload.put("timestamp", System.currentTimeMillis());
            payload.put("modo", "produccion");
            
            // Llamar al webhook del agente orquestador
            String response = llamarWebhookN8n("/orquestador-seguimientos", payload);
            
            logger.info("✅ Seguimientos iniciados para paciente {}: {}", pacienteId, response);
            
        } catch (Exception e) {
            logger.error("❌ Error iniciando seguimientos para citación {}: {}", 
                        citacionMedica.getId(), e.getMessage(), e);
            // No relanzar excepción para no afectar el guardado de la citación médica
        }
    }
    
    /**
     * NUEVO: Método para iniciar seguimientos directamente con paciente ID (útil para pruebas)
     */
    public void iniciarSeguimientosPacientePorId(Long pacienteId, Long campanaId) {
        try {
            logger.info("=== INICIANDO SEGUIMIENTOS POR PACIENTE ID ===");
            logger.info("Paciente ID: {}, Campaña ID: {}", pacienteId, campanaId);
            
            // Crear citación médica temporal para mantener la estructura
            CitacionMedica citacionTemporal = new CitacionMedica();
            citacionTemporal.setId(null); // Sin ID hardcodeado
            citacionTemporal.setPacienteId(pacienteId);
            citacionTemporal.setCampanaId(campanaId);
            citacionTemporal.setFechaCreacion(LocalDateTime.now());
            citacionTemporal.setHoraAtencion(LocalDateTime.now());
            
            // Obtener historial clínico completo directamente por paciente ID
            Map<String, Object> historialCompleto = obtenerHistorialCompleto(pacienteId, citacionTemporal);
            
            // Verificar que hay datos médicos mínimos
            if (historialCompleto.isEmpty()) {
                logger.warn("No se puede iniciar seguimientos: Sin historial clínico para paciente {}", pacienteId);
                return;
            }
            
            // Preparar payload limpio para n8n
            Map<String, Object> payload = new HashMap<>();
            payload.put("evento", "citacion_atendida");
            payload.put("citacion_id", null); // Sin citación específica
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
    private Map<String, Object> obtenerHistorialCompleto(Long pacienteId, CitacionMedica citacionMedica) {
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
            historial.put("datos_basicos", datosBasicos);
            
            // Obtener historia clínica más reciente
            List<HistoriaClinica> historias = historiaClinicaService.buscarPorPacienteId(pacienteId);
            if (historias != null && !historias.isEmpty()) {
                HistoriaClinica historiaReciente = historias.get(0); // La más reciente
                
                // Obtener datos clínicos (signos vitales, laboratorios)
                DatosClinicos datosRecientes = historiaReciente.getUltimosDatosClinicos();
                if (datosRecientes != null) {
                    Map<String, Object> datosCardiovasculares = extraerDatosCardiovasculares(datosRecientes, paciente);
                    historial.put("datos_cardiovasculares", datosCardiovasculares);
                }
                
                // Obtener triaje
                Triaje triageReciente = historiaReciente.getUltimoTriaje();
                if (triageReciente != null) {
                    Map<String, Object> datosTriaje = extraerDatosTriaje(triageReciente);
                    historial.put("triaje", datosTriaje);
                }
                
                // Obtener diagnósticos
                Diagnostico diagnosticoReciente = historiaReciente.getUltimoDiagnostico();
                if (diagnosticoReciente != null) {
                    historial.put("diagnostico", extraerDiagnosticos(diagnosticoReciente));
                }
                
                // Obtener prescripciones/medicamentos
                Prescripcion prescripcionReciente = historiaReciente.getUltimaPrescripcion();
                if (prescripcionReciente != null) {
                    Map<String, Object> prescripcionData = new HashMap<>();
                    prescripcionData.put("descripcion", prescripcionReciente.getDescripcion());
                    prescripcionData.put("dosis", prescripcionReciente.getDosis());
                    prescripcionData.put("frecuencia", prescripcionReciente.getFrecuencia());
                    prescripcionData.put("duracion", prescripcionReciente.getDuracion());
                    prescripcionData.put("medicamentos", extraerMedicamentos(prescripcionReciente));
                    historial.put("prescripcion", prescripcionData);
                }
                
                // Obtener recomendaciones
                Recomendacion recomendacionReciente = historiaReciente.getUltimaRecomendacion();
                if (recomendacionReciente != null) {
                    historial.put("recomendacion", extraerRecomendaciones(recomendacionReciente));
                }
            }
            
            // Obtener predicciones del sistema FastAPI
            try {
                Optional<List<Prediccion>> prediccionesOpt = prediccionService.buscarPorPacienteId(pacienteId);
                if (prediccionesOpt.isPresent() && !prediccionesOpt.get().isEmpty()) {
                    Prediccion prediccionReciente = prediccionesOpt.get().get(0);
                    Map<String, Object> datosPrediccion = new HashMap<>();
                    datosPrediccion.put("nivel_riesgo", prediccionReciente.getNivelRiesgo());
                    datosPrediccion.put("valor_prediccion", prediccionReciente.getValorPrediccion());
                    datosPrediccion.put("confianza", prediccionReciente.getConfianza());
                    datosPrediccion.put("fecha_prediccion", prediccionReciente.getFechaPrediccion());
                    datosPrediccion.put("modelo_version", prediccionReciente.getModeloVersion());
                    datosPrediccion.put("factores_influyentes", prediccionReciente.getFactoresInfluyentes());
                    datosPrediccion.put("recomendaciones", prediccionReciente.getRecomendaciones());
                    historial.put("prediccion_riesgo", datosPrediccion);
                }
            } catch (Exception e) {
                logger.warn("No se pudo obtener predicciones para paciente {}: {}", pacienteId, e.getMessage());
                // Continuar sin predicciones
            }
            
            logger.info("✅ Historial completo extraído para paciente {}: {} secciones", 
                       pacienteId, historial.size());
                       
        } catch (Exception e) {
            logger.error("❌ Error extrayendo historial completo para paciente {}: {}", 
                        pacienteId, e.getMessage(), e);
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
    
    private Long obtenerPacienteIdDeCitacion(CitacionMedica citacionMedica) {
        try {
            if (citacionMedica.getPacienteId() != null) {
                return citacionMedica.getPacienteId();
            }
        } catch (Exception e) {
            logger.warn("No se pudo obtener paciente ID de citación: {}", e.getMessage());
        }
        return null;
    }
    
    private Long obtenerCampanaIdDeCitacion(CitacionMedica citacionMedica) {
        try {
            if (citacionMedica.getCampanaId() != null) {
                return citacionMedica.getCampanaId();
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
    public Map<String, Object> obtenerHistorialCompletoParaPrueba(Long pacienteId, CitacionMedica citacionMedica) {
        logger.info("Obteniendo historial completo para prueba - Paciente ID: {}", pacienteId);
        return obtenerHistorialCompleto(pacienteId, citacionMedica);
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

    /**
     * Extraer datos cardiovasculares de DatosClinicos
     */
    private Map<String, Object> extraerDatosCardiovasculares(DatosClinicos datosClinicos, Paciente paciente) {
        Map<String, Object> datos = new HashMap<>();
        
        // Datos básicos del paciente
        datos.put("pacienteId", paciente.getId());
        datos.put("edad", calcularEdadPaciente(paciente));
        datos.put("sexo", mapearSexoPaciente(paciente));
        
        // Datos clínicos si están disponibles
        if (datosClinicos != null) {
            datos.put("presionSistolica", datosClinicos.getPresionSistolica() != null ? datosClinicos.getPresionSistolica() : 120);
            datos.put("presionDiastolica", datosClinicos.getPresionDiastolica() != null ? datosClinicos.getPresionDiastolica() : 80);
            
            // Usar frecuencia cardiaca promedio si están disponibles
            Double frecuenciaCardiaca = null;
            if (datosClinicos.getFrecuenciaCardiacaMin() != null && datosClinicos.getFrecuenciaCardiacaMax() != null) {
                frecuenciaCardiaca = (datosClinicos.getFrecuenciaCardiacaMin() + datosClinicos.getFrecuenciaCardiacaMax()) / 2.0;
            } else if (datosClinicos.getFrecuenciaCardiacaMin() != null) {
                frecuenciaCardiaca = datosClinicos.getFrecuenciaCardiacaMin();
            } else if (datosClinicos.getFrecuenciaCardiacaMax() != null) {
                frecuenciaCardiaca = datosClinicos.getFrecuenciaCardiacaMax();
            }
            datos.put("frecuenciaCardiaca", frecuenciaCardiaca != null ? frecuenciaCardiaca : 70);
            
            datos.put("colesterolTotal", datosClinicos.getColesterolTotal() != null ? datosClinicos.getColesterolTotal() : 200);
            datos.put("colesterolHDL", datosClinicos.getHdl() != null ? datosClinicos.getHdl() : 50);
            datos.put("saturacionOxigeno", datosClinicos.getSaturacionOxigeno() != null ? datosClinicos.getSaturacionOxigeno() : 98);
            datos.put("temperatura", datosClinicos.getTemperatura() != null ? datosClinicos.getTemperatura() : 36.5);
            
            // Valores por defecto para peso, altura e IMC (no disponibles en DatosClinicos)
            datos.put("peso", 70.0);
            datos.put("altura", 170.0);
            datos.put("imc", 23.0);
        } else {
            // Valores por defecto si no hay datos clínicos
            datos.put("presionSistolica", 120);
            datos.put("presionDiastolica", 80);
            datos.put("frecuenciaCardiaca", 70);
            datos.put("peso", 70.0);
            datos.put("altura", 170.0);
            datos.put("imc", 23.0);
            datos.put("colesterolTotal", 200);
            datos.put("colesterolHDL", 50);
            datos.put("saturacionOxigeno", 98);
            datos.put("temperatura", 36.5);
        }
        
        // Datos adicionales para análisis cardiovascular (valores por defecto)
        datos.put("colesterolLDL", 100); // Por defecto normal
        datos.put("glicemia", 90); // Por defecto normal
        datos.put("fumador", false); // Por defecto no fumador
        datos.put("actividadFisica", "SEDENTARIO"); // Por defecto sedentario
        
        return datos;
    }
    
    /**
     * Extraer datos de triaje
     */
    private Map<String, Object> extraerDatosTriaje(Triaje triaje) {
        Map<String, Object> datos = new HashMap<>();
        
        if (triaje != null) {
            // Usar descripción en lugar de síntomas
            datos.put("sintomas", triaje.getDescripcion() != null ? triaje.getDescripcion() : "Sin descripción disponible");
            datos.put("fechaTriaje", triaje.getFechaTriaje());
            
            // Determinar nivel de prioridad basado en síntomas cardiovasculares
            String nivelPrioridad = "NORMAL";
            if (triaje.isDolorPecho() || triaje.isDolorIrradiado() || triaje.isSudoracion()) {
                nivelPrioridad = "ALTA";
            } else if (triaje.isHipertension() || triaje.isAntecedentesCardiacos() || triaje.isDiabetes()) {
                nivelPrioridad = "MEDIA";
            }
            datos.put("nivelPrioridad", nivelPrioridad);
            
            // Agregar datos adicionales del triaje cardiovascular
            datos.put("edad", triaje.getEdad());
            datos.put("peso", triaje.getPeso());
            datos.put("estatura", triaje.getEstatura());
            datos.put("factores_riesgo", Map.of(
                "tabaquismo", triaje.isTabaquismo(),
                "diabetes", triaje.isDiabetes(),
                "hipertension", triaje.isHipertension(),
                "antecedentes_cardiacos", triaje.isAntecedentesCardiacos()
            ));
        } else {
            datos.put("nivelPrioridad", "NORMAL");
            datos.put("sintomas", "Sin triaje disponible");
            datos.put("fechaTriaje", null);
        }
        
        return datos;
    }
} 