package com.healink.integrador.domain.seguimientos;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;
import com.healink.integrador.domain.atenciones_medicas.AtencionMedica;
import com.healink.integrador.domain.atenciones_medicas.AtencionMedicaService;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class SeguimientoService extends ServicioGenerico<Seguimiento> {

    private static final Logger logger = LoggerFactory.getLogger(SeguimientoService.class);
    private final SeguimientoRepository seguimientoRepository;
    private final AtencionMedicaService atencionMedicaService;

    public SeguimientoService(SeguimientoRepository seguimientoRepository,
                             AtencionMedicaService atencionMedicaService) {
        super(seguimientoRepository);
        this.seguimientoRepository = seguimientoRepository;
        this.atencionMedicaService = atencionMedicaService;
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
    public List<Seguimiento> buscarPorAtencionId(Long atencion_id) {
        return seguimientoRepository.findByAtencionId(atencion_id);
    }

    @Transactional(readOnly = true)
    public Page<Seguimiento> buscarPorAtencionId(Long diagnostico_id, Pageable pageable) {
        return seguimientoRepository.findByAtencionId(diagnostico_id, pageable);
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

            // Obtener atención médica (puede ser null para pruebas)
            AtencionMedica atencion = null;
            if (atencionId != null) {
                try {
                    atencion = atencionMedicaService.obtenerPorId(atencionId);
                } catch (EntityNotFoundException e) {
                    logger.warn("Atención médica {} no encontrada, continuando sin atención", atencionId);
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
            List<Long> seguimientosCreados = crearSeguimientosEnBD(seguimientos, atencion, pacienteId, analisisIA);

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
                                           AtencionMedica atencion, Long pacienteId, 
                                           Map<String, Object> analisisIA) {
        List<Long> idsCreados = new ArrayList<>();
        
        for (Map<String, Object> segN8n : seguimientosN8n) {
            try {
                Seguimiento seguimiento = new Seguimiento();
                
                // Relación con atención médica
                seguimiento.setAtencion(atencion);
                
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
}