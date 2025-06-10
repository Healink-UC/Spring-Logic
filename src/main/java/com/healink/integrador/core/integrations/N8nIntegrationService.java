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
     * Obtener historial clínico completo - SOLO DATOS, sin procesamiento
     */
    private Map<String, Object> obtenerHistorialCompleto(Long pacienteId, AtencionMedica atencionMedica) {
        Map<String, Object> historial = new HashMap<>();
        
        try {
            // Datos básicos del paciente
            Paciente paciente = pacienteService.obtenerPorId(pacienteId);
            if (paciente != null) {
                historial.put("paciente_id", pacienteId);
                historial.put("datos_basicos", extraerDatosBasicos(paciente));
            }
            
            // Historia clínica
            List<HistoriaClinica> historiasClinicas = historiaClinicaService.buscarPorPacienteId(pacienteId);
            HistoriaClinica historiaActual = historiasClinicas.isEmpty() ? null : historiasClinicas.get(0);
            
            if (historiaActual != null) {
                historial.put("historia_clinica_id", historiaActual.getId());
                
                // Datos clínicos RAW
                if (historiaActual.getUltimosDatosClinicos() != null) {
                    historial.put("datos_clinicos", extraerDatosClinicos(historiaActual.getUltimosDatosClinicos()));
                }
                
                // Triaje RAW
                if (historiaActual.getUltimoTriaje() != null) {
                    historial.put("triaje_inicial", extraerDatosTriaje(historiaActual.getUltimoTriaje()));
                }
                
                // Diagnósticos RAW
                if (historiaActual.getUltimoDiagnostico() != null) {
                    historial.put("diagnosticos", extraerDiagnosticos(historiaActual.getUltimoDiagnostico()));
                }
                
                // Recomendaciones RAW
                if (historiaActual.getUltimaRecomendacion() != null) {
                    historial.put("recomendaciones_medicas", extraerRecomendaciones(historiaActual.getUltimaRecomendacion()));
                }
                
                // Prescripciones RAW
                if (historiaActual.getUltimaPrescripcion() != null) {
                    historial.put("medicamentos_prescritos", extraerPrescripciones(historiaActual.getUltimaPrescripcion()));
                }
                
                // Predicción IA RAW
                historial.put("prediccion_ia", extraerPrediccionIA(historiaActual));
            }
            
            // **NUEVO: Predicciones desde FastAPI-Back**
            Long campanaId = obtenerCampanaIdDeAtencion(atencionMedica);
            Map<String, Object> prediccionesFastAPI = obtenerPrediccionesFastAPI(pacienteId, campanaId);
            if (!prediccionesFastAPI.isEmpty()) {
                historial.put("predicciones_fastapi", prediccionesFastAPI);
            }
            
            // Metadatos de la atención
            if (atencionMedica != null) {
                historial.put("atencion_id", atencionMedica.getId());
                historial.put("campana_id", campanaId);
            }
            
        } catch (Exception e) {
            logger.error("Error obteniendo historial completo para paciente {}: {}", 
                        pacienteId, e.getMessage(), e);
        }
        
        return historial;
    }
    
    /**
     * NUEVO: Obtener predicciones calculadas por FastAPI-Back
     */
    private Map<String, Object> obtenerPrediccionesFastAPI(Long pacienteId, Long campanaId) {
        Map<String, Object> predicciones = new HashMap<>();
        
        try {
            // Obtener predicciones por tipo
            List<Prediccion> prediccionesRiesgoCV = prediccionService.buscarPorPacienteYTipo(pacienteId, "RIESGO_CV");
            List<Prediccion> prediccionesAsistencia = prediccionService.buscarPorPacienteYTipo(pacienteId, "ASISTENCIA");
            List<Prediccion> prediccionesHospitalizacion = prediccionService.buscarPorPacienteYTipo(pacienteId, "HOSPITALIZACION");
            
            // Predicción de Riesgo CV (más reciente)
            if (!prediccionesRiesgoCV.isEmpty()) {
                Prediccion riesgoCV = prediccionesRiesgoCV.get(0); // Más reciente
                Map<String, Object> datosRiesgoCV = new HashMap<>();
                datosRiesgoCV.put("valor_prediccion", riesgoCV.getValorPrediccion()); // 0-100
                datosRiesgoCV.put("nivel_riesgo", riesgoCV.getNivelRiesgo().toString()); // BAJO, MODERADO, ALTO, CRITICO
                datosRiesgoCV.put("confianza", riesgoCV.getConfianza());
                datosRiesgoCV.put("factores_influyentes", riesgoCV.getFactoresInfluyentes()); // JSON
                datosRiesgoCV.put("recomendaciones", riesgoCV.getRecomendaciones()); // JSON
                datosRiesgoCV.put("fecha_prediccion", riesgoCV.getFechaPrediccion().toString());
                datosRiesgoCV.put("modelo_version", riesgoCV.getModeloVersion());
                predicciones.put("riesgo_cardiovascular", datosRiesgoCV);
            }
            
            // Predicción de Asistencia (para priorización)
            if (!prediccionesAsistencia.isEmpty()) {
                Prediccion asistencia = prediccionesAsistencia.get(0);
                Map<String, Object> datosAsistencia = new HashMap<>();
                datosAsistencia.put("probabilidad_asistencia", asistencia.getValorPrediccion());
                datosAsistencia.put("confianza", asistencia.getConfianza());
                datosAsistencia.put("fecha_prediccion", asistencia.getFechaPrediccion().toString());
                predicciones.put("asistencia", datosAsistencia);
            }
            
            // Predicción de Hospitalización
            if (!prediccionesHospitalizacion.isEmpty()) {
                Prediccion hospitalizacion = prediccionesHospitalizacion.get(0);
                Map<String, Object> datosHospitalizacion = new HashMap<>();
                datosHospitalizacion.put("probabilidad_hospitalizacion", hospitalizacion.getValorPrediccion());
                datosHospitalizacion.put("nivel_riesgo", hospitalizacion.getNivelRiesgo() != null ? 
                    hospitalizacion.getNivelRiesgo().toString() : null);
                datosHospitalizacion.put("confianza", hospitalizacion.getConfianza());
                datosHospitalizacion.put("fecha_prediccion", hospitalizacion.getFechaPrediccion().toString());
                predicciones.put("hospitalizacion", datosHospitalizacion);
            }
            
        } catch (Exception e) {
            logger.error("Error obteniendo predicciones FastAPI para paciente {}: {}", 
                        pacienteId, e.getMessage(), e);
        }
        
        return predicciones;
    }
    
    // ====== MÉTODOS DE EXTRACCIÓN PURA DE DATOS ======
    
    private Map<String, Object> extraerDatosBasicos(Paciente paciente) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("edad", calcularEdad(paciente));
        datos.put("genero", paciente.getGenero() != null ? paciente.getGenero().toString() : "M");
        datos.put("direccion", paciente.getDireccion() != null ? paciente.getDireccion() : "");
        datos.put("tipo_sangre", paciente.getTipoSangre() != null ? paciente.getTipoSangre().toString() : "");
        datos.put("usuario_id", paciente.getUsuarioId());
        return datos;
    }
    
    private Integer calcularEdad(Paciente paciente) {
        if (paciente.getFechaNacimiento() != null) {
            return Period.between(paciente.getFechaNacimiento(), java.time.LocalDate.now()).getYears();
        }
        return null;
    }
    
    private Map<String, Object> extraerDatosClinicos(DatosClinicos datosClinicos) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("presion_arterial_sistolica", datosClinicos.getPresionSistolica());
        datos.put("presion_arterial_diastolica", datosClinicos.getPresionDiastolica());
        datos.put("colesterol_total", datosClinicos.getColesterolTotal());
        datos.put("hdl", datosClinicos.getHdl());
        datos.put("frecuencia_cardiaca_min", datosClinicos.getFrecuenciaCardiacaMin());
        datos.put("frecuencia_cardiaca_max", datosClinicos.getFrecuenciaCardiacaMax());
        datos.put("saturacion_oxigeno", datosClinicos.getSaturacionOxigeno());
        datos.put("temperatura", datosClinicos.getTemperatura());
        datos.put("fecha_medicion", datosClinicos.getFechaMedicion() != null ? 
                 datosClinicos.getFechaMedicion().toString() : null);
        datos.put("observaciones", datosClinicos.getObservaciones());
        return datos;
    }
    
    private Map<String, Object> extraerDatosTriaje(Triaje triaje) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("fecha_triaje", triaje.getFechaTriaje() != null ? triaje.getFechaTriaje().toString() : null);
        datos.put("edad", triaje.getEdad());
        datos.put("peso", triaje.getPeso());
        datos.put("estatura", triaje.getEstatura());
        
        // Factores de riesgo - RAW data
        Map<String, Boolean> factoresRiesgo = new HashMap<>();
        factoresRiesgo.put("tabaquismo", triaje.isTabaquismo());
        factoresRiesgo.put("alcoholismo", triaje.isAlcoholismo());
        factoresRiesgo.put("diabetes", triaje.isDiabetes());
        factoresRiesgo.put("hipertension", triaje.isHipertension());
        factoresRiesgo.put("antecedentes_cardiacos", triaje.isAntecedentesCardiacos());
        factoresRiesgo.put("actividad_fisica", triaje.isActividadFisica());
        datos.put("factores_riesgo", factoresRiesgo);
        
        // Síntomas reportados - RAW data
        Map<String, Boolean> sintomas = new HashMap<>();
        sintomas.put("dolor_pecho", triaje.isDolorPecho());
        sintomas.put("dolor_irradiado", triaje.isDolorIrradiado());
        sintomas.put("sudoracion", triaje.isSudoracion());
        sintomas.put("nauseas", triaje.isNauseas());
        datos.put("sintomas", sintomas);
        
        datos.put("descripcion", triaje.getDescripcion());
        return datos;
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
    
    private List<Map<String, Object>> extraerPrescripciones(Prescripcion prescripcion) {
        List<Map<String, Object>> medicamentos = new ArrayList<>();
        // TODO: Implementar según estructura real de Prescripcion
        Map<String, Object> med = new HashMap<>();
        med.put("id", prescripcion.getId());
        med.put("raw_data", "Implementar extracción de prescripciones");
        medicamentos.add(med);
        return medicamentos;
    }
    
    private Map<String, Object> extraerPrediccionIA(HistoriaClinica historia) {
        Map<String, Object> prediccion = new HashMap<>();
        prediccion.put("probabilidad_rehospitalizacion", historia.getProbRehospitalizacion());
        prediccion.put("fecha_prediccion", LocalDateTime.now().toString());
        return prediccion;
    }
    
    // ====== MÉTODOS AUXILIARES ======
    
    private Long obtenerPacienteIdDeAtencion(AtencionMedica atencionMedica) {
        try {
            CitacionMedica citacion = citacionMedicaService.obtenerPorId(atencionMedica.getCitacionId());
            return citacion != null ? citacion.getPacienteId() : null;
        } catch (Exception e) {
            logger.error("Error obteniendo paciente ID de atención {}: {}", 
                        atencionMedica.getId(), e.getMessage());
            return null;
        }
    }
    
    private Long obtenerCampanaIdDeAtencion(AtencionMedica atencionMedica) {
        try {
            // CAMBIO: Sin citación, intentar obtener campaña de otra forma o usar null
            if (atencionMedica.getCitacionId() == null) {
                logger.info("Sin citación asociada a atención {}, campaña será null", atencionMedica.getId());
                return null; // Sin campaña hardcodeada
            }
            
            CitacionMedica citacion = citacionMedicaService.obtenerPorId(atencionMedica.getCitacionId());
            if (citacion != null && citacion.getCampanaId() != null) {
                logger.info("Campaña real encontrada: {}", citacion.getCampanaId());
                return citacion.getCampanaId();
            } else {
                logger.warn("Citación {} existe pero sin campaña asociada", atencionMedica.getCitacionId());
                return null; // Sin campaña hardcodeada - usar null
            }
        } catch (Exception e) {
            logger.error("Error obteniendo campaña ID de atención {}: {}", 
                        atencionMedica.getId(), e.getMessage());
            return null; // CAMBIO: Eliminar valor hardcodeado 123L
        }
    }
    
    private String llamarWebhookN8n(String endpoint, Map<String, Object> payload) {
        try {
            String url = n8nBaseUrl + endpoint;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            String jsonPayload = objectMapper.writeValueAsString(payload);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
            
            // LOGGING DETALLADO PARA DEBUGGING
            logger.info("=== LLAMADA A N8N DEBUGGING ===");
            logger.info("URL completa: {}", url);
            logger.info("Headers: {}", headers);
            logger.info("Payload JSON length: {} chars", jsonPayload.length());
            logger.info("Payload preview: {}", jsonPayload.length() > 200 ? 
                       jsonPayload.substring(0, 200) + "..." : jsonPayload);
            
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
} 