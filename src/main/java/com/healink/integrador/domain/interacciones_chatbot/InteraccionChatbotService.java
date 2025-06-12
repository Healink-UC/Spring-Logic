package com.healink.integrador.domain.interacciones_chatbot;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.service.ServicioGenerico;
import com.healink.integrador.domain.seguimientos.Seguimiento;
import com.healink.integrador.domain.seguimientos.SeguimientoService;
import com.healink.integrador.domain.paciente.Paciente;
import com.healink.integrador.domain.paciente.PacienteService;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class InteraccionChatbotService extends ServicioGenerico<InteraccionChatbot> {

    private static final Logger logger = LoggerFactory.getLogger(InteraccionChatbotService.class);

    private final InteraccionChatbotRepository interaccionChatbotRepository;
    private final SeguimientoService seguimientoService;
    private final PacienteService pacienteService;
    private final ObjectMapper objectMapper;

    public InteraccionChatbotService(InteraccionChatbotRepository interaccionChatbotRepository,
                                   SeguimientoService seguimientoService,
                                   PacienteService pacienteService,
                                   ObjectMapper objectMapper) {
        super(interaccionChatbotRepository);
        this.interaccionChatbotRepository = interaccionChatbotRepository;
        this.seguimientoService = seguimientoService;
        this.pacienteService = pacienteService;
        this.objectMapper = objectMapper;
    }

    @Override
    public InteraccionChatbot guardar(InteraccionChatbot interaccionChatbot) {
        return super.guardar(interaccionChatbot);
    }

    @Override
    public InteraccionChatbot obtenerPorId(Long id) {
        return interaccionChatbotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interacción con el chat bot no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<InteraccionChatbot> buscarPorSeguimientoId(Long seguimiento_id) {
        return interaccionChatbotRepository.findBySeguimientoId(seguimiento_id);
    }

    @Transactional(readOnly = true)
    public Page<InteraccionChatbot> buscarPorSeguimientoId(Long seguimiento_id, Pageable pageable) {
        return interaccionChatbotRepository.findBySeguimientoId(seguimiento_id, pageable);
    }

    /**
     * NUEVO: Guardar respuestas del cuestionario como interacción chatbot
     */
    @Transactional
    public InteraccionChatbot guardarRespuestasCuestionario(Long seguimientoId, Map<String, Object> respuestas) {
        try {
            logger.info("💾 Guardando respuestas de cuestionario para seguimiento: {}", seguimientoId);
            
            // Obtener seguimiento y paciente
            Seguimiento seguimiento = seguimientoService.obtenerPorId(seguimientoId);
            Paciente paciente = obtenerPacienteDeSeguimiento(seguimiento);
            
            // Crear interacción chatbot
            InteraccionChatbot interaccion = new InteraccionChatbot();
            interaccion.setSeguimiento(seguimiento);
            interaccion.setPaciente(paciente);
            interaccion.setFecha_hora(Timestamp.valueOf(LocalDateTime.now()));
            
            // Construir entrada (resumen de preguntas respondidas)
            StringBuilder entradaBuilder = new StringBuilder();
            entradaBuilder.append("Cuestionario de seguimiento completado:\n");
            respuestas.forEach((pregunta, respuesta) -> 
                entradaBuilder.append("- ").append(pregunta).append(": ").append(respuesta).append("\n"));
            
            interaccion.setEntrada(entradaBuilder.toString());
            interaccion.setRespuesta("Respuestas del cuestionario registradas correctamente");
            interaccion.setIntent_detectado("cuestionario_seguimiento");
            
            // Convertir respuestas a JsonNode para contexto
            JsonNode contextJson = objectMapper.valueToTree(Map.of(
                "tipo_interaccion", "cuestionario_seguimiento",
                "respuestas", respuestas,
                "fecha_completado", LocalDateTime.now().toString(),
                "seguimiento_id", seguimientoId
            ));
            
            interaccion.setContexto_conversacion(contextJson);
            
            return guardar(interaccion);
            
        } catch (Exception e) {
            logger.error("❌ Error guardando respuestas de cuestionario: {}", e.getMessage(), e);
            throw new RuntimeException("Error guardando respuestas del cuestionario", e);
        }
    }

    /**
     * NUEVO: Analizar respuestas y generar recomendaciones
     */
    @Transactional(readOnly = true)
    public Map<String, Object> analizarRespuestasCuestionario(Long seguimientoId) {
        logger.info("🔍 Analizando respuestas del seguimiento: {}", seguimientoId);
        
        List<InteraccionChatbot> interacciones = buscarPorSeguimientoId(seguimientoId);
        
        if (interacciones.isEmpty()) {
            return Map.of(
                "estado", "sin_respuestas",
                "mensaje", "No hay respuestas registradas para este seguimiento"
            );
        }
        
        // Obtener la interacción más reciente del cuestionario
        InteraccionChatbot ultimaInteraccion = interacciones.stream()
            .filter(i -> "cuestionario_seguimiento".equals(i.getIntent_detectado()))
            .findFirst()
            .orElse(null);
            
        if (ultimaInteraccion == null) {
            return Map.of(
                "estado", "sin_cuestionario",
                "mensaje", "No se encontró cuestionario completado"
            );
        }
        
        // Extraer respuestas del contexto JSON
        JsonNode contexto = ultimaInteraccion.getContexto_conversacion();
        Map<String, Object> respuestas = extractRespuestas(contexto);
        
        // Análisis básico de las respuestas
        Map<String, Object> analisis = realizarAnalisisBasico(respuestas);
        
        return Map.of(
            "estado", "analizado",
            "seguimiento_id", seguimientoId,
            "fecha_analisis", LocalDateTime.now(),
            "respuestas", respuestas,
            "analisis", analisis
        );
    }

    /**
     * Obtener paciente desde seguimiento
     */
    private Paciente obtenerPacienteDeSeguimiento(Seguimiento seguimiento) {
        // Obtener paciente desde la citación médica del seguimiento
        if (seguimiento.getCitacion() != null && seguimiento.getCitacion().getPacienteId() != null) {
            return pacienteService.obtenerPorId(seguimiento.getCitacion().getPacienteId());
        }
        throw new RuntimeException("No se pudo obtener el paciente del seguimiento - citación no disponible");
    }

    /**
     * Extraer respuestas del contexto JSON
     */
    private Map<String, Object> extractRespuestas(JsonNode contexto) {
        Map<String, Object> respuestas = new HashMap<>();
        
        if (contexto != null && contexto.has("respuestas")) {
            JsonNode respuestasNode = contexto.get("respuestas");
            respuestasNode.fields().forEachRemaining(entry -> 
                respuestas.put(entry.getKey(), entry.getValue().asText())
            );
        }
        
        return respuestas;
    }

    /**
     * Realizar análisis básico de respuestas
     */
    private Map<String, Object> realizarAnalisisBasico(Map<String, Object> respuestas) {
        Map<String, Object> analisis = new HashMap<>();
        
        // Análisis de adherencia a medicamentos
        String adherencia = (String) respuestas.get("adherencia_medicamentos");
        if (adherencia != null) {
            if (adherencia.contains("Sí, todos los días")) {
                analisis.put("adherencia_nivel", "EXCELENTE");
                analisis.put("adherencia_score", 100);
            } else if (adherencia.contains("Casi siempre")) {
                analisis.put("adherencia_nivel", "BUENO");
                analisis.put("adherencia_score", 80);
            } else {
                analisis.put("adherencia_nivel", "PREOCUPANTE");
                analisis.put("adherencia_score", 40);
            }
        }
        
        // Análisis de síntomas
        String sintomas = (String) respuestas.get("sintomas_cardiovasculares");
        if (sintomas != null) {
            if (sintomas.contains("Ninguno")) {
                analisis.put("sintomas_nivel", "ESTABLE");
                analisis.put("urgencia", "BAJA");
            } else if (sintomas.contains("Dolor en el pecho") || sintomas.contains("Dificultad para respirar")) {
                analisis.put("sintomas_nivel", "ALERTA");
                analisis.put("urgencia", "ALTA");
            } else {
                analisis.put("sintomas_nivel", "MONITOREO");
                analisis.put("urgencia", "MEDIA");
            }
        }
        
        // Recomendación general
        String urgencia = (String) analisis.get("urgencia");
        if ("ALTA".equals(urgencia)) {
            analisis.put("recomendacion", "Contactar inmediatamente con su médico");
            analisis.put("seguimiento_sugerido", "24_horas");
        } else if ("MEDIA".equals(urgencia)) {
            analisis.put("recomendacion", "Monitorear síntomas y contactar médico si empeoran");
            analisis.put("seguimiento_sugerido", "7_dias");
        } else {
            analisis.put("recomendacion", "Continuar con el tratamiento actual");
            analisis.put("seguimiento_sugerido", "30_dias");
        }
        
        return analisis;
    }
}