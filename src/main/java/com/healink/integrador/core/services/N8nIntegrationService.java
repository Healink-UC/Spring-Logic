package com.healink.integrador.core.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.healink.integrador.domain.seguimientos.SeguimientoRepository;
import com.healink.integrador.domain.seguimientos.Seguimiento;
import com.healink.integrador.domain.paciente.PacienteService;
import com.healink.integrador.domain.paciente.Paciente;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.Instant;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * 🏥 SERVICIO EXTENDIDO PARA WORKFLOWS ESPECÍFICOS DE N8N
 * 
 * Métodos adicionales para:
 * 1. Generar cuestionarios personalizados
 * 2. Analizar respuestas de cuestionarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class N8nIntegrationService {

    private final RestTemplate restTemplate;
    private final SeguimientoRepository seguimientoRepository;
    private final PacienteService pacienteService;

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
            // 1. Obtener datos completos del paciente
            Map<String, Object> datosCompletos = construirDatosCompletosPaciente(pacienteId, seguimientoId);
            
            // 2. Llamar al webhook de n8n para generación de cuestionarios
            String n8nUrl = "http://localhost:5678/webhook/agente1-cardiovascular";
            
            log.info("🎯 Generando cuestionario personalizado para paciente {} - seguimiento {}", 
                     pacienteId, seguimientoId);
            
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Crear entity con el body
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(datosCompletos, headers);
            
            // Realizar llamada POST
            ResponseEntity<String> response = restTemplate.exchange(
                n8nUrl, 
                HttpMethod.POST, 
                entity, 
                String.class
            );
            
            log.info("✅ Cuestionario personalizado generado exitosamente");
            return response.getBody();
            
        } catch (Exception e) {
            log.error("❌ Error generando cuestionario personalizado: {}", e.getMessage(), e);
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
            // 1. Construir datos completos para análisis
            Map<String, Object> datosAnalisis = construirDatosAnalisisCompleto(
                pacienteId, seguimientoId, respuestas, campanaId);
            
            // 2. Llamar al webhook de n8n para análisis de respuestas
            String n8nUrl = "http://localhost:5678/webhook/analizar-respuestas-cuestionario";
            
            log.info("🧠 Analizando respuestas de cuestionario - Paciente: {}, Seguimiento: {}", 
                     pacienteId, seguimientoId);
            
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Crear entity con el body
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(datosAnalisis, headers);
            
            // Realizar llamada POST
            ResponseEntity<String> response = restTemplate.exchange(
                n8nUrl, 
                HttpMethod.POST, 
                entity, 
                String.class
            );
            
            log.info("✅ Análisis de respuestas completado exitosamente");
            return response.getBody();
            
        } catch (Exception e) {
            log.error("❌ Error analizando respuestas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al analizar respuestas del cuestionario: " + e.getMessage());
        }
    }

    /**
     * 📊 CONSTRUIR DATOS COMPLETOS DEL PACIENTE PARA CUESTIONARIO
     */
    private Map<String, Object> construirDatosCompletosPaciente(Long pacienteId, Long seguimientoId) {
        // Obtener paciente
        Paciente paciente = pacienteService.obtenerPorId(pacienteId);
        if (paciente == null) {
            throw new RuntimeException("Paciente no encontrado: " + pacienteId);
        }
        
        // Obtener seguimiento
        Seguimiento seguimiento = seguimientoRepository.findById(seguimientoId)
            .orElseThrow(() -> new RuntimeException("Seguimiento no encontrado: " + seguimientoId));
        
        // Datos básicos del paciente
        String nombreCompleto = "Paciente " + pacienteId; // Valor por defecto
        String documento = "Sin documento";
        
        // Obtener datos del usuario relacionado si está disponible
        if (paciente.getUsuario() != null) {
            nombreCompleto = paciente.getUsuario().getNombres() + " " + paciente.getUsuario().getApellidos();
            documento = paciente.getUsuario().getIdentificacion();
        }
        
        Map<String, Object> datosBasicos = Map.of(
            "nombre", nombreCompleto,
            "edad", calcularEdad(paciente.getFechaNacimiento()),
            "genero", paciente.getGenero() != null ? paciente.getGenero().toString() : "NO_ESPECIFICADO",
            "documento", documento
        );
        
        // Datos cardiovasculares básicos (usando valores por defecto si no están disponibles)
        Map<String, Object> datosCardiovasculares = Map.of(
            "presionSistolica", 120, // Por defecto, se podría obtener del triaje más reciente
            "presionDiastolica", 80,
            "colesterolTotal", 200,
            "diabetes", false // Se podría obtener de diagnósticos
        );
        
        // Datos clínicos completos (simplificados para esta implementación)
        Map<String, Object> datosCompletos = Map.of(
            "diagnosticos", obtenerDiagnosticosSimulados(),
            "prescripciones", obtenerPrescripcionesSimuladas(),
            "triaje", obtenerTriajeSimulado(),
            "factores_riesgo_cardiovascular", obtenerFactoresRiesgoSimulados()
        );
        
        // Contexto del seguimiento
        Map<String, Object> seguimientoContext = Map.of(
            "seguimiento_id", seguimiento.getId(),
            "tipo", seguimiento.getTipo().toString(),
            "prioridad", seguimiento.getPrioridad() != null ? seguimiento.getPrioridad().toString() : "MEDIA",
            "dias_desde_programacion", calcularDiasDesdeProgramacion(seguimiento.getFecha_programada()),
            "resultado_analisis_ia", seguimiento.getResultado() != null ? 
                seguimiento.getResultado() : "Seguimiento cardiovascular - Control de presión arterial",
            "notas_seguimiento", seguimiento.getNotas() != null ? 
                seguimiento.getNotas() : "Seguimiento médico regular"
        );
        
        return Map.of(
            "paciente_id", pacienteId,
            "datos_basicos", datosBasicos,
            "datos_cardiovasculares", datosCardiovasculares,
            "datos_completos", datosCompletos,
            "seguimiento_context", seguimientoContext,
            "objetivo_cuestionario", "seguimiento_especifico",
            "timestamp", Instant.now().toString()
        );
    }

    /**
     * 🔍 CONSTRUIR DATOS COMPLETOS PARA ANÁLISIS DE RESPUESTAS
     */
    private Map<String, Object> construirDatosAnalisisCompleto(Long pacienteId, Long seguimientoId, 
                                                              Map<String, Object> respuestas, Long campanaId) {
        
        // Datos actualizados del paciente (simulados)
        Map<String, Object> datosActualizados = Map.of(
            "triaje", obtenerTriajeSimulado(),
            "diagnosticos", obtenerDiagnosticosSimulados(),
            "prescripciones", obtenerPrescripcionesSimuladas(),
            "recomendaciones", obtenerRecomendacionesSimuladas()
        );
        
        // Información de la campaña (simplificada)
        Map<String, Object> campanaInfo = Map.of(
            "nombre", "Campaña Cardiovascular",
            "objetivo", "Seguimiento y control cardiovascular",
            "fecha_inicio", "2024-01-01"
        );
        
        // Seguimientos anteriores (simulados)
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
            "factores_detectados", List.of("presion_arterial_elevada")
        );
        
        Map<String, Object> factoresInfluyentes = Map.of(
            "factores_riesgo", List.of("hipertension"),
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
            "timestamp_analisis", Instant.now().toString()
        );
    }

    // ===== MÉTODOS AUXILIARES =====
    
    private int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return 45; // Valor por defecto
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    
    private long calcularDiasDesdeProgramacion(LocalDate fechaProgramada) {
        if (fechaProgramada == null) return 0;
        return Period.between(fechaProgramada, LocalDate.now()).getDays();
    }
    
    // Métodos que devuelven datos simulados (en producción se conectarían a los servicios reales)
    
    private List<Map<String, Object>> obtenerDiagnosticosSimulados() {
        return List.of(
            Map.of(
                "codigo_cie10", "I10",
                "descripcion", "Hipertensión esencial",
                "fecha_diagnostico", "2024-01-15"
            )
        );
    }
    
    private List<Map<String, Object>> obtenerPrescripcionesSimuladas() {
        return List.of(
            Map.of(
                "medicamento", "Losartán 50mg",
                "dosis", "50mg",
                "frecuencia", "1 vez al día",
                "duracion", "30 días"
            )
        );
    }
    
    private Map<String, Object> obtenerTriajeSimulado() {
        return Map.of(
            "presion_sistolica", 130,
            "presion_diastolica", 85,
            "frecuencia_cardiaca", 75,
            "temperatura", 36.5,
            "saturacion_oxigeno", 98,
            "fecha_triaje", LocalDate.now().toString()
        );
    }
    
    private Map<String, Object> obtenerFactoresRiesgoSimulados() {
        return Map.of(
            "hipertension", true,
            "diabetes", false,
            "tabaquismo", false,
            "obesidad", false,
            "antecedentes_familiares", true
        );
    }
    
    private List<Map<String, Object>> obtenerRecomendacionesSimuladas() {
        return List.of(
            Map.of(
                "descripcion", "Mantener dieta baja en sodio",
                "tipo", "ESTILO_VIDA",
                "prioridad", "ALTA"
            ),
            Map.of(
                "descripcion", "Ejercicio cardiovascular 30 min diarios",
                "tipo", "ACTIVIDAD_FISICA",
                "prioridad", "MEDIA"
            )
        );
    }
} 