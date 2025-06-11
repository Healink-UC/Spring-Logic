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

            // Extraer análisis de IA desde n8n
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
                seguimiento.setFecha_realizada(null);
                
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
                
                // ✨ NUEVO: Notas enriquecidas con análisis de IA
                String notasBase = String.format(
                    "Seguimiento #%d creado automáticamente por n8n.\nPaciente: %d\nPrioridad: %s",
                    extractInteger(segN8n, "numeroSeguimiento"),
                    pacienteId,
                    (String) segN8n.get("prioridad")
                );
                
                // Agregar análisis de IA si está disponible
                StringBuilder notasCompletas = new StringBuilder(notasBase);
                if (analisisIA != null && !analisisIA.isEmpty()) {
                    notasCompletas.append("\n\n🤖 ANÁLISIS DE IA:");
                    
                    String nivelRiesgo = (String) analisisIA.get("nivelRiesgo");
                    if (nivelRiesgo != null) {
                        notasCompletas.append("\n• Nivel de Riesgo: ").append(nivelRiesgo);
                    }
                    
                    List<String> factoresRiesgo = (List<String>) analisisIA.get("factoresRiesgo");
                    if (factoresRiesgo != null && !factoresRiesgo.isEmpty()) {
                        notasCompletas.append("\n• Factores de Riesgo: ").append(String.join(", ", factoresRiesgo));
                    }
                    
                    List<String> recomendaciones = (List<String>) analisisIA.get("recomendaciones");
                    if (recomendaciones != null && !recomendaciones.isEmpty()) {
                        notasCompletas.append("\n• Recomendaciones: ").append(String.join(", ", recomendaciones));
                    }
                    
                    String cluster = (String) analisisIA.get("cluster");
                    if (cluster != null) {
                        notasCompletas.append("\n• Cluster K-means: ").append(cluster);
                    }
                    
                    Double probabilidadRiesgo = extractDouble(analisisIA, "probabilidadRiesgo");
                    if (probabilidadRiesgo != null) {
                        notasCompletas.append("\n• Probabilidad de Riesgo: ").append(String.format("%.1f%%", probabilidadRiesgo * 100));
                    }
                }
                
                seguimiento.setNotas(notasCompletas.toString());
                
                // Estado programado
                seguimiento.setEstado(EstadoSeguimiento.PROGRAMADO);
                
                // Prioridad
                String prioridadStr = (String) segN8n.get("prioridad");
                NivelPrioridad prioridad = mapearPrioridad(prioridadStr);
                seguimiento.setPrioridad(prioridad);
                
                // Guardar en BD
                Seguimiento seguimientoGuardado = this.guardar(seguimiento);
                idsCreados.add(seguimientoGuardado.getId());
                
                logger.debug("✅ Seguimiento {} creado para fecha {} con análisis IA", 
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
    
    private Double extractDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}