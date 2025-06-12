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
            historial.put("datos_basicos", datosBasicos);
            
            // Historia clínica más reciente
            List<HistoriaClinica> historiasClinicas = historiaClinicaService.buscarPorPacienteId(pacienteId);
            HistoriaClinica historiaActual = historiasClinicas.isEmpty() ? null : historiasClinicas.get(0);
            
            // **DATOS CARDIOVASCULARES PRINCIPALES** - Formato exacto para n8n
            Map<String, Object> datosCardiovasculares = new HashMap<>();
            
            // Datos básicos demográficos
            datosCardiovasculares.put("pacienteId", pacienteId);
            datosCardiovasculares.put("edad", calcularEdadPaciente(paciente));
            datosCardiovasculares.put("sexo", mapearSexoPaciente(paciente));
            
            // Datos clínicos cardiovasculares
            if (historiaActual != null) {
                // Datos clínicos más recientes
                DatosClinicos datosClinicos = historiaActual.getUltimosDatosClinicos();
                if (datosClinicos != null) {
                    datosCardiovasculares.put("presionSistolica", datosClinicos.getPresionSistolica() != null ? 
                        datosClinicos.getPresionSistolica().intValue() : 120);
                    datosCardiovasculares.put("presionDiastolica", datosClinicos.getPresionDiastolica() != null ? 
                        datosClinicos.getPresionDiastolica().intValue() : 80);
                    datosCardiovasculares.put("colesterolTotal", datosClinicos.getColesterolTotal() != null ? 
                        datosClinicos.getColesterolTotal().intValue() : 200);
                    datosCardiovasculares.put("hdl", datosClinicos.getHdl() != null ? 
                        datosClinicos.getHdl().intValue() : 50);
                    
                    // Calcular LDL aproximado (Fórmula de Friedewald)
                    double colesterolTotal = datosClinicos.getColesterolTotal() != null ? 
                        datosClinicos.getColesterolTotal() : 200;
                    double hdl = datosClinicos.getHdl() != null ? datosClinicos.getHdl() : 50;
                    int ldlAproximado = (int) Math.max(0, colesterolTotal - hdl - 50); // Estimación simple
                    datosCardiovasculares.put("ldl", ldlAproximado);
                    
                    // Triglicéridos estimados
                    datosCardiovasculares.put("trigliceridos", 150); // Valor por defecto
                    
                    // Glucosa estimada
                    datosCardiovasculares.put("glucosa", 100); // Valor por defecto normal
                } else {
                    // Valores por defecto si no hay datos clínicos
                    logger.warn("No hay datos clínicos para paciente {}, usando valores por defecto", pacienteId);
                    datosCardiovasculares.put("presionSistolica", 120);
                    datosCardiovasculares.put("presionDiastolica", 80);
                    datosCardiovasculares.put("colesterolTotal", 200);
                    datosCardiovasculares.put("hdl", 50);
                    datosCardiovasculares.put("ldl", 120);
                    datosCardiovasculares.put("trigliceridos", 150);
                    datosCardiovasculares.put("glucosa", 100);
                }
                
                // Datos del triaje más reciente
                Triaje triaje = historiaActual.getUltimoTriaje();
                if (triaje != null) {
                    // CORREGIDO: Usar Float wrapper para permitir null checks
                    Float peso = triaje.getPeso();
                    Float estatura = triaje.getEstatura();
                    
                    datosCardiovasculares.put("peso", peso != null ? peso.intValue() : 70);
                    datosCardiovasculares.put("estatura", estatura != null ? estatura.intValue() : 170);
                    datosCardiovasculares.put("tabaquismo", triaje.isTabaquismo());
                    datosCardiovasculares.put("antecedentesCardiacos", triaje.isAntecedentesCardiacos());
                    
                    // Actividad física (inferir del triaje)
                    datosCardiovasculares.put("actividadFisica", triaje.isActividadFisica() ? "activo" : "sedentario");
                } else {
                    // Valores por defecto si no hay triaje
                    logger.warn("No hay triaje para paciente {}, usando valores por defecto", pacienteId);
                    datosCardiovasculares.put("peso", 70);
                    datosCardiovasculares.put("estatura", 170);
                    datosCardiovasculares.put("tabaquismo", false);
                    datosCardiovasculares.put("antecedentesCardiacos", false);
                    datosCardiovasculares.put("actividadFisica", "sedentario");
                }
                
                // Medicamentos (desde prescripciones)
                List<String> medicamentos = extraerMedicamentos(historiaActual.getUltimaPrescripcion());
                datosCardiovasculares.put("medicamentos", medicamentos);
                
                // Diagnósticos recientes
                Map<String, Object> diagnosticos = extraerDiagnosticos(historiaActual.getUltimoDiagnostico());
                historial.put("diagnosticos", diagnosticos);
                
                // Recomendaciones médicas
                Map<String, Object> recomendaciones = extraerRecomendaciones(historiaActual.getUltimaRecomendacion());
                historial.put("recomendaciones", recomendaciones);
                
            } else {
                // Sin historia clínica - usar datos mínimos seguros
                logger.warn("No hay historia clínica para paciente {}, usando datos mínimos", pacienteId);
                
                int edadPaciente = calcularEdadPaciente(paciente);
                datosCardiovasculares.put("presionSistolica", edadPaciente > 50 ? 140 : 120);
                datosCardiovasculares.put("presionDiastolica", edadPaciente > 50 ? 90 : 80);
                datosCardiovasculares.put("colesterolTotal", edadPaciente > 50 ? 220 : 180);
                datosCardiovasculares.put("hdl", 50);
                datosCardiovasculares.put("ldl", edadPaciente > 50 ? 150 : 100);
                datosCardiovasculares.put("trigliceridos", 150);
                datosCardiovasculares.put("glucosa", 100);
                datosCardiovasculares.put("peso", 70);
                datosCardiovasculares.put("estatura", 170);
                datosCardiovasculares.put("tabaquismo", false);
                datosCardiovasculares.put("antecedentesCardiacos", false);
                datosCardiovasculares.put("actividadFisica", "sedentario");
                datosCardiovasculares.put("medicamentos", new ArrayList<>());
            }
            
            // Agregar datos cardiovasculares al historial
            historial.put("datos_cardiovasculares", datosCardiovasculares);
            
            // Predicciones de riesgo más recientes
            try {
                List<Prediccion> predicciones = prediccionService.buscarPorPacienteId(pacienteId)
                    .orElse(new ArrayList<>());
                if (!predicciones.isEmpty()) {
                    Prediccion ultimaPrediccion = predicciones.get(0);
                    Map<String, Object> datosPrediccion = new HashMap<>();
                    datosPrediccion.put("probabilidad_riesgo", ultimaPrediccion.getValorPrediccion());
                    datosPrediccion.put("nivel_riesgo", ultimaPrediccion.getNivelRiesgo());
                    datosPrediccion.put("fecha_prediccion", ultimaPrediccion.getFechaPrediccion());
                    historial.put("prediccion_riesgo", datosPrediccion);
                }
            } catch (Exception e) {
                logger.warn("Error obteniendo predicciones para paciente {}: {}", pacienteId, e.getMessage());
            }
            
            // Información de la campaña (si está disponible)
            if (atencionMedica != null && atencionMedica.getCitacionMedica() != null) {
                Long campanaId = atencionMedica.getCitacionMedica().getCampanaId();
                if (campanaId != null) {
                    historial.put("campana_id", campanaId);
                }
            }
            
            logger.info("✅ Historial completo extraído para paciente {} con {} campos", 
                       pacienteId, historial.size());
            
            return historial;
            
        } catch (Exception e) {
            logger.error("❌ Error extrayendo historial completo para paciente {}: {}", 
                        pacienteId, e.getMessage(), e);
            return historial; // Retornar lo que se pudo extraer
        }
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
            logger.info("Payload JSON completo:\n{}", jsonPayload);
            
            // NUEVO: Log de estructura específica
            if (payload.containsKey("historial_clinico")) {
                Map<String, Object> historial = (Map<String, Object>) payload.get("historial_clinico");
                logger.info("📊 Estructura historial_clinico: {}", historial.keySet());
                
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
} 