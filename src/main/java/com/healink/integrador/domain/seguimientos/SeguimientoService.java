package com.healink.integrador.domain.seguimientos;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;
import com.healink.integrador.core.integrations.N8nIntegrationService;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedica;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedicaService;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class SeguimientoService extends ServicioGenerico<Seguimiento> {

    private static final Logger logger = LoggerFactory.getLogger(SeguimientoService.class);
    private final SeguimientoRepository seguimientoRepository;
    private final CitacionMedicaService citacionMedicaService;
    private final N8nIntegrationService n8nIntegrationService;

    public SeguimientoService(SeguimientoRepository seguimientoRepository,
                             CitacionMedicaService citacionMedicaService,
                             N8nIntegrationService n8nIntegrationService) {
        super(seguimientoRepository);
        this.seguimientoRepository = seguimientoRepository;
        this.citacionMedicaService = citacionMedicaService;
        this.n8nIntegrationService = n8nIntegrationService;
    }

    @Override
    public Seguimiento guardar(Seguimiento seguimiento) {
        return super.guardar(seguimiento);
    }

    @Override
    public Seguimiento obtenerPorId(Long id) {
        return seguimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seguimiento no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Seguimiento> buscarPorCitacionId(Long citacionId) {
        return seguimientoRepository.findByCitacionId(citacionId);
    }

    @Transactional(readOnly = true)
    public Page<Seguimiento> buscarPorCitacionId(Long citacionId, Pageable pageable) {
        return seguimientoRepository.findByCitacionId(citacionId, pageable);
    }

    /**
     * 🎯 MÉTODO PRINCIPAL: Procesa y guarda seguimientos desde n8n
     */
    @Transactional
    public Map<String, Object> procesarSeguimientosDesdeN8n(Map<String, Object> datosN8n) {
        try {
            logger.info("🔄 Procesando seguimientos desde n8n: {}", datosN8n.keySet());
            
            // Extraer datos principales
            Long pacienteId = extractLong(datosN8n, "pacienteId");
            Long atencionId = extractLong(datosN8n, "atencion_id");
            
            if (pacienteId == null) {
                throw new IllegalArgumentException("pacienteId es requerido");
            }

            // Obtener citación médica (puede ser null para pruebas)
            CitacionMedica citacion = null;
            if (atencionId != null) {
                try {
                    citacion = citacionMedicaService.obtenerPorId(atencionId);
                } catch (EntityNotFoundException e) {
                    logger.warn("Citación médica {} no encontrada, continuando sin citación", atencionId);
                }
            }

            // Extraer plan de seguimiento
            Map<String, Object> planSeguimiento = (Map<String, Object>) datosN8n.get("planSeguimiento");
            List<Map<String, Object>> seguimientos = planSeguimiento != null ? 
                (List<Map<String, Object>>) planSeguimiento.get("seguimientos") : null;

            if (seguimientos == null || seguimientos.isEmpty()) {
                logger.warn("No hay seguimientos para procesar para paciente {}", pacienteId);
                return Map.of("status", "warning", "mensaje", "No hay seguimientos para crear");
            }

            // NUEVO: Extraer análisis de IA para incluir en notas
            Map<String, Object> analisisIA = (Map<String, Object>) datosN8n.get("analisisIA");

            // Crear seguimientos en la base de datos
            List<Long> seguimientosCreados = crearSeguimientosEnBD(seguimientos, citacion, pacienteId, analisisIA);

            // Preparar respuesta
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("pacienteId", pacienteId);
            resultado.put("atencionId", atencionId);
            resultado.put("seguimientosCreados", seguimientosCreados.size());
            resultado.put("ids", seguimientosCreados);
            resultado.put("fechaProcesamiento", LocalDateTime.now());
            resultado.put("status", "exitoso");

            logger.info("✅ {} seguimientos creados para paciente {}", seguimientosCreados.size(), pacienteId);
            
            return resultado;
            
        } catch (Exception e) {
            logger.error("❌ Error procesando seguimientos desde n8n: {}", e.getMessage(), e);
            throw new RuntimeException("Error procesando seguimientos desde n8n: " + e.getMessage(), e);
        }
    }

    /**
     * 🏥 Crear seguimientos reales en la base de datos
     */
    private List<Long> crearSeguimientosEnBD(List<Map<String, Object>> seguimientosN8n, 
                                           CitacionMedica citacion, Long pacienteId, 
                                           Map<String, Object> analisisIA) {
        List<Long> idsCreados = new ArrayList<>();
        
        for (Map<String, Object> segN8n : seguimientosN8n) {
            try {
                Seguimiento seguimiento = new Seguimiento();
                
                // Relación con citación médica
                seguimiento.setCitacion(citacion);
                
                // Fechas
                String fechaProgramadaStr = (String) segN8n.get("fechaProgramada");
                if (fechaProgramadaStr != null) {
                    // Convertir de ISO string a LocalDate
                    LocalDate fechaProgramada = LocalDate.parse(fechaProgramadaStr.substring(0, 10));
                    seguimiento.setFecha_programada(fechaProgramada);
                } else {
                    // Calcular fecha usando días después
                    Integer diasDespues = extractInteger(segN8n, "diasDespues");
                    if (diasDespues != null) {
                        seguimiento.setFecha_programada(LocalDate.now().plusDays(diasDespues));
                    } else {
                        seguimiento.setFecha_programada(LocalDate.now().plusDays(7)); // Default 7 días
                    }
                }
                
                // No establecer fecha_realizada (null hasta que se complete)
                // seguimiento.setFecha_realizada(null); // Ya es null por defecto
                
                // Tipo de seguimiento (chatbot por defecto desde n8n)
                String tipoStr = (String) segN8n.get("tipo");
                if ("chatbot_cuestionario".equals(tipoStr) || tipoStr == null) {
                    seguimiento.setTipo(TipoSeguimiento.CHATBOT);
                } else {
                    // Mapear otros tipos si es necesario
                    seguimiento.setTipo(TipoSeguimiento.CHATBOT);
                }
                
                // Resultado inicial
                String mensaje = (String) segN8n.get("mensaje");
                if (mensaje != null) {
                    seguimiento.setResultado(mensaje);
                } else {
                    seguimiento.setResultado("Seguimiento programado por sistema automático");
                }
                
                // MEJORADO: Notas desde n8n con análisis de IA incluido
                StringBuilder notasBuilder = new StringBuilder();
                notasBuilder.append(String.format(
                    "Seguimiento #%d creado automáticamente por n8n.\n" +
                    "Paciente: %d | Prioridad: %s\n",
                    extractInteger(segN8n, "numeroSeguimiento"),
                    pacienteId,
                    (String) segN8n.get("prioridad")
                ));
                
                // Agregar mensaje del seguimiento
                if (mensaje != null) {
                    notasBuilder.append("Mensaje: ").append(mensaje).append("\n");
                }
                
                // NUEVO: Agregar análisis de IA a las notas
                if (analisisIA != null && !analisisIA.isEmpty()) {
                    notasBuilder.append("\n--- ANÁLISIS DE IA ---\n");
                    
                    // Nivel de riesgo
                    String nivelRiesgo = (String) analisisIA.get("nivelRiesgo");
                    if (nivelRiesgo != null) {
                        notasBuilder.append("Nivel de Riesgo: ").append(nivelRiesgo).append("\n");
                    }
                    
                    // Factores de riesgo
                    List<String> factoresRiesgo = (List<String>) analisisIA.get("factoresRiesgo");
                    if (factoresRiesgo != null && !factoresRiesgo.isEmpty()) {
                        notasBuilder.append("Factores de Riesgo: ").append(String.join(", ", factoresRiesgo)).append("\n");
                    }
                    
                    // Recomendaciones
                    List<String> recomendaciones = (List<String>) analisisIA.get("recomendaciones");
                    if (recomendaciones != null && !recomendaciones.isEmpty()) {
                        notasBuilder.append("Recomendaciones IA: ").append(String.join(", ", recomendaciones)).append("\n");
                    }
                    
                    // Cluster asignado
                    String clusterAsignado = (String) analisisIA.get("clusterAsignado");
                    if (clusterAsignado != null) {
                        notasBuilder.append("Cluster Asignado: ").append(clusterAsignado).append("\n");
                    }
                    
                    // Probabilidad de riesgo
                    Object probabilidadRiesgo = analisisIA.get("probabilidadRiesgo");
                    if (probabilidadRiesgo != null) {
                        notasBuilder.append("Probabilidad de Riesgo: ").append(probabilidadRiesgo).append("\n");
                    }
                }
                
                seguimiento.setNotas(notasBuilder.toString());
                
                // Estado programado
                seguimiento.setEstado(EstadoSeguimiento.PROGRAMADO);
                
                // Prioridad
                String prioridadStr = (String) segN8n.get("prioridad");
                NivelPrioridad prioridad = mapearPrioridad(prioridadStr);
                seguimiento.setPrioridad(prioridad);
                
                // Guardar en BD
                Seguimiento seguimientoGuardado = this.guardar(seguimiento);
                idsCreados.add(seguimientoGuardado.getId());
                
                logger.debug("✅ Seguimiento {} creado para fecha {}", 
                           seguimientoGuardado.getId(), seguimiento.getFecha_programada());
                
            } catch (Exception e) {
                logger.error("❌ Error creando seguimiento individual: {}", e.getMessage(), e);
                // Continuar con el siguiente seguimiento
            }
        }
        
        return idsCreados;
    }
    
    /**
     * 🎨 Mapear prioridad de n8n a enum
     */
    private NivelPrioridad mapearPrioridad(String prioridadN8n) {
        if (prioridadN8n == null) return NivelPrioridad.MEDIA;
        
        switch (prioridadN8n.toUpperCase()) {
            case "ALTA":
            case "HIGH":
            case "CRITICA":
                return NivelPrioridad.ALTA;
            case "BAJA":
            case "LOW":
                return NivelPrioridad.BAJA;
            case "MEDIA":
            case "MEDIUM":
            default:
                return NivelPrioridad.MEDIA;
        }
    }
    
    // Métodos auxiliares para extracción de datos
    private Long extractLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    private Integer extractInteger(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * NUEVO: Obtener seguimientos pendientes para un paciente (OPTIMIZADO)
     */
    @Transactional(readOnly = true)
    public List<Seguimiento> obtenerSeguimientosPendientesPorPaciente(Long pacienteId) {
        logger.info("🔍 Buscando seguimientos pendientes para paciente: {}", pacienteId);
        
        LocalDate hoy = LocalDate.now();
        
        // Usar query optimizada del repositorio
        return seguimientoRepository.findSeguimientosPendientesByPacienteId(pacienteId, hoy);
    }

    /**
     * NUEVO: Obtener todos los seguimientos de un paciente (OPTIMIZADO)
     */
    @Transactional(readOnly = true)
    public List<Seguimiento> obtenerSeguimientosPorPaciente(Long pacienteId) {
        logger.info("🔍 Buscando todos los seguimientos para paciente: {}", pacienteId);
        
        // Usar query optimizada del repositorio
        return seguimientoRepository.findByPacienteId(pacienteId);
    }

    /**
     * NUEVO: Obtener seguimientos disponibles HOY para un paciente (para habilitar cuestionarios)
     */
    @Transactional(readOnly = true)
    public List<Seguimiento> obtenerSeguimientosDisponiblesHoy(Long pacienteId) {
        logger.info("📅 Buscando seguimientos disponibles HOY para paciente: {}", pacienteId);
        
        LocalDate hoy = LocalDate.now();
        
        // Usar query optimizada del repositorio
        return seguimientoRepository.findSeguimientosDisponiblesHoyByPacienteId(pacienteId, hoy);
    }

    /**
     * NUEVO: Completar seguimiento con respuestas del cuestionario
     */
    @Transactional
    public Seguimiento completarSeguimiento(Long seguimientoId, Map<String, Object> respuestasEvaluacion) {
        logger.info("📝 Completando seguimiento {} con respuestas", seguimientoId);
        
        Seguimiento seguimiento = obtenerPorId(seguimientoId);
        
        // Actualizar estado y fecha de realización
        seguimiento.setEstado(EstadoSeguimiento.REALIZADO);
        seguimiento.setFecha_realizada(LocalDate.now());
        
        // Procesar respuestas y generar resultado
        StringBuilder resultadoBuilder = new StringBuilder();
        resultadoBuilder.append("Seguimiento completado el ").append(LocalDate.now()).append(".\n");
        
        if (respuestasEvaluacion != null && !respuestasEvaluacion.isEmpty()) {
            resultadoBuilder.append("Respuestas del paciente:\n");
            respuestasEvaluacion.forEach((pregunta, respuesta) -> 
                resultadoBuilder.append("- ").append(pregunta).append(": ").append(respuesta).append("\n"));
        }
        
        // Actualizar resultado con las respuestas
        seguimiento.setResultado(resultadoBuilder.toString());
        
        // Agregar evaluación a las notas existentes
        String notasActualizadas = seguimiento.getNotas() + 
            "\n\n=== RESPUESTAS PACIENTE ===\n" + 
            "Fecha: " + LocalDateTime.now() + "\n" +
            "Respuestas: " + respuestasEvaluacion.toString();
        
        seguimiento.setNotas(notasActualizadas);
        
        return guardar(seguimiento);
    }

    /**
     * NUEVO: Generar cuestionario personalizado usando n8n
     */
    @Transactional(readOnly = true)
    public Map<String, Object> generarCuestionarioPersonalizado(Long seguimientoId) {
        logger.info("🎯 Generando cuestionario personalizado para seguimiento: {}", seguimientoId);
        
        Seguimiento seguimiento = obtenerPorId(seguimientoId);
        
        // Por ahora, retornar un cuestionario básico
        // TODO: Integrar con el workflow del compañero en n8n
        Map<String, Object> cuestionario = new HashMap<>();
        
        Map<String, Object> cuestionarioData = new HashMap<>();
        cuestionarioData.put("titulo", "Seguimiento Cardiovascular");
        cuestionarioData.put("instrucciones", "Por favor responda las siguientes preguntas sobre su estado de salud");
        
        List<Map<String, Object>> preguntas = new ArrayList<>();
        
        // Pregunta básica sobre medicamentos
        Map<String, Object> pregunta1 = new HashMap<>();
        pregunta1.put("id", "adherencia_medicamentos");
        pregunta1.put("pregunta", "¿Ha tomado sus medicamentos como se le indicó?");
        pregunta1.put("tipo", "opcion_multiple");
        pregunta1.put("opciones", List.of("Sí, todos los días", "Casi siempre", "A veces", "Pocas veces", "No los he tomado"));
        pregunta1.put("requerida", true);
        preguntas.add(pregunta1);
        
        // Pregunta sobre síntomas
        Map<String, Object> pregunta2 = new HashMap<>();
        pregunta2.put("id", "sintomas_cardiovasculares");
        pregunta2.put("pregunta", "¿Ha experimentado alguno de estos síntomas en los últimos días?");
        pregunta2.put("tipo", "multiple_seleccion");
        pregunta2.put("opciones", List.of("Dolor en el pecho", "Dificultad para respirar", "Palpitaciones", "Mareos", "Ninguno"));
        pregunta2.put("requerida", true);
        preguntas.add(pregunta2);
        
        cuestionarioData.put("preguntas", preguntas);
        
        cuestionario.put("cuestionario", cuestionarioData);
        cuestionario.put("seguimiento_id", seguimientoId);
        cuestionario.put("fecha_generacion", LocalDateTime.now());
        cuestionario.put("estado", "listo_para_responder");
        
        return cuestionario;
    }

    /**
     * NUEVO: Preparar datos para el workflow del compañero
     */
    public Map<String, Object> prepararDatosParaWorkflowCompanero(Seguimiento seguimiento) {
        try {
            logger.info("📋 Preparando datos para workflow compañero - Seguimiento: {}", seguimiento.getId());
            
            // Obtener paciente del seguimiento
            Long pacienteId = obtenerPacienteIdDeSeguimiento(seguimiento);
            
            // Obtener datos básicos del paciente (simulados por ahora)
            Map<String, Object> datosBasicos = Map.of(
                "nombre", "Paciente " + pacienteId,
                "edad", 45, // TODO: Obtener edad real del paciente
                "telefono", "123456789"
            );
            
            // Preparar datos cardiovasculares estándar (consistentes con workflow principal)
            Map<String, Object> datosCardiovasculares = Map.of(
                "presionSistolica", 120, // TODO: Obtener datos reales
                "presionDiastolica", 80,
                "colesterolTotal", 180,
                "diabetes", false,
                "hipertension", true
            );
            
            // CLAVE: Contexto específico del seguimiento con notas y resultado
            Map<String, Object> seguimientoContext = Map.of(
                "seguimiento_id", seguimiento.getId(),
                "tipo", seguimiento.getTipo().toString(),
                "prioridad", seguimiento.getPrioridad().toString(),
                "fecha_programada", seguimiento.getFecha_programada().toString(),
                "dias_desde_programacion", calcularDiasDesdeProgramacion(seguimiento),
                // INFORMACIÓN CLAVE: Análisis previo del workflow principal
                "resultado_analisis_ia", seguimiento.getResultado(),
                "notas_seguimiento", seguimiento.getNotas(),
                "estado_actual", seguimiento.getEstado().toString()
            );
            
            // Crear prompt especializado basado en el análisis previo
            String promptEspecializado = construirPromptEspecializado(seguimiento);
            
            // Estructura final estandarizada
            return Map.of(
                "paciente_id", pacienteId,
                "datos_basicos", datosBasicos,
                "datos_cardiovasculares", datosCardiovasculares, // ✅ Nombre consistente
                "seguimiento_context", seguimientoContext,
                "prompt_especializado", promptEspecializado,
                "objetivo_cuestionario", determinarObjetivoCuestionario(seguimiento)
            );
            
        } catch (Exception e) {
            logger.error("❌ Error preparando datos para workflow compañero: {}", e.getMessage(), e);
            throw new RuntimeException("Error preparando datos para workflow compañero", e);
        }
    }

    /**
     * NUEVO: Generar cuestionario con workflow del compañero usando n8n
     */
    public Map<String, Object> generarCuestionarioConWorkflowCompanero(Map<String, Object> datosWorkflow) {
        try {
            logger.info("🤖 Generando cuestionario con workflow compañero...");
            
            // Intentar usar n8n primero
            try {
                Map<String, Object> respuestaN8n = n8nIntegrationService.generarCuestionarioConWorkflowCompanero(datosWorkflow);
                
                if (respuestaN8n.get("success") != Boolean.FALSE) {
                    logger.info("✅ Cuestionario generado exitosamente con n8n");
                    return respuestaN8n;
                }
            } catch (Exception e) {
                logger.warn("⚠️ Error con n8n, usando generación local: {}", e.getMessage());
            }
            
            // Fallback: generar cuestionario localmente basado en el análisis previo
            logger.info("🔄 Generando cuestionario localmente como fallback");
            
            Map<String, Object> seguimientoContext = (Map<String, Object>) datosWorkflow.get("seguimiento_context");
            String resultadoAnalisis = (String) seguimientoContext.get("resultado_analisis_ia");
            String notasSeguimiento = (String) seguimientoContext.get("notas_seguimiento");
            String objetivo = (String) datosWorkflow.get("objetivo_cuestionario");
            
            // Generar preguntas específicas basadas en el análisis previo
            List<Map<String, Object>> preguntas = generarPreguntasEspecificas(resultadoAnalisis, notasSeguimiento, objetivo);
            
            return Map.of(
                "success", true,
                "cuestionario", Map.of(
                    "titulo", "Seguimiento Cardiovascular Personalizado",
                    "instrucciones", "Estas preguntas están diseñadas específicamente para su situación de salud actual",
                    "preguntas", preguntas,
                    "basado_en_analisis", resultadoAnalisis.substring(0, Math.min(100, resultadoAnalisis.length())) + "..."
                ),
                "metadata", Map.of(
                    "generado_en", LocalDateTime.now(),
                    "seguimiento_id", seguimientoContext.get("seguimiento_id"),
                    "tipo_seguimiento", seguimientoContext.get("tipo"),
                    "generado_con", "local_fallback"
                )
            );
            
        } catch (Exception e) {
            logger.error("❌ Error generando cuestionario con workflow: {}", e.getMessage(), e);
            throw new RuntimeException("Error generando cuestionario", e);
        }
    }

    /**
     * Construir prompt especializado basado en análisis previo
     */
    private String construirPromptEspecializado(Seguimiento seguimiento) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Genera un cuestionario de seguimiento cardiovascular personalizado.\n\n");
        prompt.append("CONTEXTO DEL PACIENTE:\n");
        prompt.append("- Tipo de seguimiento: ").append(seguimiento.getTipo()).append("\n");
        prompt.append("- Prioridad: ").append(seguimiento.getPrioridad()).append("\n");
        prompt.append("- Análisis previo de IA: ").append(seguimiento.getResultado()).append("\n");
        prompt.append("- Notas específicas: ").append(seguimiento.getNotas()).append("\n\n");
        
        prompt.append("INSTRUCCIONES:\n");
        prompt.append("1. Genera 3-5 preguntas específicas basadas en el análisis previo\n");
        prompt.append("2. Enfócate en los factores de riesgo identificados\n");
        prompt.append("3. Incluye preguntas sobre adherencia a recomendaciones\n");
        prompt.append("4. Adapta el lenguaje al nivel de prioridad\n");
        prompt.append("5. Formato JSON con preguntas de opción múltiple\n");
        
        return prompt.toString();
    }

    /**
     * Determinar objetivo del cuestionario basado en seguimiento
     */
    private String determinarObjetivoCuestionario(Seguimiento seguimiento) {
        switch (seguimiento.getPrioridad()) {
            case ALTA:
                return "Evaluación urgente de síntomas y adherencia al tratamiento";
            case MEDIA:
                return "Monitoreo regular del progreso y detección temprana de cambios";
            case BAJA:
                return "Seguimiento preventivo y refuerzo de hábitos saludables";
            default:
                return "Evaluación general del estado de salud cardiovascular";
        }
    }

    /**
     * Generar preguntas específicas basadas en análisis previo
     */
    private List<Map<String, Object>> generarPreguntasEspecificas(String resultadoAnalisis, String notas, String objetivo) {
        List<Map<String, Object>> preguntas = new ArrayList<>();
        
        // Pregunta 1: Adherencia a medicamentos (siempre relevante)
        preguntas.add(Map.of(
            "id", "adherencia_medicamentos",
            "pregunta", "¿Ha tomado sus medicamentos cardiovasculares según las indicaciones?",
            "tipo", "opcion_multiple",
            "opciones", List.of(
                "Sí, todos los días sin excepción",
                "Casi siempre, solo he olvidado 1-2 veces",
                "A veces olvido tomarlos",
                "Frecuentemente olvido o decido no tomarlos"
            ),
            "requerida", true
        ));
        
        // Pregunta 2: Síntomas específicos basados en el análisis
        List<String> opcionesSintomas = new ArrayList<>();
        opcionesSintomas.add("Ningún síntoma");
        
        if (resultadoAnalisis.toLowerCase().contains("presión") || resultadoAnalisis.toLowerCase().contains("hipertensión")) {
            opcionesSintomas.add("Dolor de cabeza frecuente");
            opcionesSintomas.add("Sensación de presión en el pecho");
        }
        
        if (resultadoAnalisis.toLowerCase().contains("colesterol") || resultadoAnalisis.toLowerCase().contains("grasa")) {
            opcionesSintomas.add("Fatiga inusual");
            opcionesSintomas.add("Dificultad para respirar con esfuerzo leve");
        }
        
        opcionesSintomas.add("Dolor en el pecho");
        opcionesSintomas.add("Palpitaciones o latidos irregulares");
        opcionesSintomas.add("Mareos o desmayos");
        
        preguntas.add(Map.of(
            "id", "sintomas_cardiovasculares",
            "pregunta", "¿Ha experimentado alguno de estos síntomas en los últimos días?",
            "tipo", "multiple_seleccion",
            "opciones", opcionesSintomas,
            "requerida", true
        ));
        
        // Pregunta 3: Adherencia a recomendaciones específicas (basada en notas)
        String preguntaRecomendaciones = "¿Ha seguido las recomendaciones médicas indicadas?";
        List<String> opcionesRecomendaciones = List.of(
            "Sí, he seguido todas las recomendaciones",
            "He seguido la mayoría, con algunas excepciones",
            "Solo he seguido algunas recomendaciones",
            "Me ha costado seguir las recomendaciones"
        );
        
        if (notas.toLowerCase().contains("ejercicio") || notas.toLowerCase().contains("actividad física")) {
            preguntaRecomendaciones = "¿Ha realizado la actividad física recomendada?";
            opcionesRecomendaciones = List.of(
                "Sí, hago ejercicio regularmente según lo indicado",
                "Hago ejercicio, pero menos de lo recomendado",
                "Ocasionalmente hago algo de ejercicio",
                "No he podido hacer ejercicio"
            );
        } else if (notas.toLowerCase().contains("dieta") || notas.toLowerCase().contains("alimentación")) {
            preguntaRecomendaciones = "¿Ha seguido las recomendaciones dietéticas?";
            opcionesRecomendaciones = List.of(
                "Sí, he cambiado mi alimentación completamente",
                "He hecho algunos cambios importantes",
                "He intentado cambiar pero me cuesta",
                "No he podido cambiar mi alimentación"
            );
        }
        
        preguntas.add(Map.of(
            "id", "adherencia_recomendaciones",
            "pregunta", preguntaRecomendaciones,
            "tipo", "opcion_multiple",
            "opciones", opcionesRecomendaciones,
            "requerida", true
        ));
        
        return preguntas;
    }

    /**
     * Calcular días desde la programación del seguimiento
     */
    private long calcularDiasDesdeProgramacion(Seguimiento seguimiento) {
        return ChronoUnit.DAYS.between(seguimiento.getFecha_programada(), LocalDate.now());
    }

    /**
     * Obtener ID del paciente desde el seguimiento
     */
    private Long obtenerPacienteIdDeSeguimiento(Seguimiento seguimiento) {
        if (seguimiento.getCitacion() != null) {
            return seguimiento.getCitacion().getPacienteId();
        }
        // TODO: Implementar lógica alternativa para obtener paciente
        return 1L; // Valor por defecto para pruebas
    }
}