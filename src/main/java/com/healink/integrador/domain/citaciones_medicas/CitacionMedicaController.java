package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.controller.ControladorGenerico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citaciones-medicas")
@Tag(name = "Citaciones Médicas", description = "API para gestión de citaciones médicas de pacientes")
public class CitacionMedicaController extends ControladorGenerico<CitacionMedica, CitacionMedicaDTO> {

        private final CitacionMedicaService citacionMedicaService;
        private final CitacionMedicaMapper citacionMedicaMapper;

        public CitacionMedicaController(CitacionMedicaService citacionMedicaService,
                        CitacionMedicaMapper citacionMedicaMapper) {
                super(citacionMedicaService, citacionMedicaMapper);
                this.citacionMedicaService = citacionMedicaService;
                this.citacionMedicaMapper = citacionMedicaMapper;
        }

        @GetMapping("/paciente/{pacienteId}")
        @Operation(summary = "Buscar citaciones medicas por paciente", description = "Obtiene todos los citaciones medicas de un paciente específico")
        public ResponseEntity<List<CitacionMedicaDTO>> buscarPorPacienteId(@PathVariable Long pacienteId) {
                return citacionMedicaService.getByPacienteId(pacienteId)
                                .map(citaciones_medicas -> ResponseEntity.ok(
                                                citaciones_medicas.stream()
                                                                .map(citacion_medica -> citacionMedicaMapper
                                                                                .aDTO(citacion_medica))
                                                                .collect(Collectors.toList())))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/medico/{medicoId}")
        @Operation(summary = "Buscar citaciones medicas por medico", description = "Obtiene todos los citaciones medicas de un medico específico")
        public ResponseEntity<List<CitacionMedicaDTO>> buscarPorMedicoId(@PathVariable Long medicoId) {
                return citacionMedicaService.getByMedicoId(medicoId)
                                .map(citaciones_medicas -> ResponseEntity.ok(
                                                citaciones_medicas.stream()
                                                                .map(citacion_medica -> citacionMedicaMapper
                                                                                .aDTO(citacion_medica))
                                                                .collect(Collectors.toList())))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/campana/{campanaId}")
        @Operation(summary = "Buscar citaciones medicas por medico", description = "Obtiene todos los citaciones medicas de un medico específico")
        public ResponseEntity<List<CitacionMedicaDTO>> buscarPorCamapnaId(@PathVariable Long campanaId) {
                return citacionMedicaService.getByCampanaId(campanaId)
                                .map(citaciones_medicas -> ResponseEntity.ok(
                                                citaciones_medicas.stream()
                                                                .map(citacion_medica -> citacionMedicaMapper
                                                                                .aDTO(citacion_medica))
                                                                .collect(Collectors.toList())))
                                .orElse(ResponseEntity.notFound().build());
        }

        /**
         * 🚀 NUEVO: Marcar citación como atendida (activa seguimientos automáticamente)
         */
        @PutMapping("/{citacionId}/marcar-atendida")
        @Operation(summary = "Marcar citación como atendida", 
                   description = "Marca una citación como atendida y activa automáticamente los seguimientos cardiovasculares")
        public ResponseEntity<Map<String, Object>> marcarComoAtendida(@PathVariable Long citacionId) {
                try {
                        CitacionMedica citacionActualizada = citacionMedicaService.marcarComoAtendida(citacionId);
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Citación marcada como atendida y seguimientos iniciados automáticamente");
                        response.put("citacion", citacionMedicaMapper.aDTO(citacionActualizada));
                        response.put("paciente_id", citacionActualizada.getPacienteId());
                        response.put("campana_id", citacionActualizada.getCampanaId());
                        response.put("hora_atencion", citacionActualizada.getHoraAtencion());
                        response.put("timestamp", LocalDateTime.now());
                        
                        return ResponseEntity.ok(response);
                        
                } catch (Exception e) {
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("success", false);
                        errorResponse.put("message", "Error marcando citación como atendida: " + e.getMessage());
                        errorResponse.put("citacion_id", citacionId);
                        
                        return ResponseEntity.badRequest().body(errorResponse);
                }
        }

        /**
         * 📋 NUEVO: Completar atención médica con hora específica
         */
        @PutMapping("/{citacionId}/completar-atencion")
        @Operation(summary = "Completar atención médica", 
                   description = "Completa una atención médica con hora específica y activa seguimientos")
        public ResponseEntity<Map<String, Object>> completarAtencionMedica(
                        @PathVariable Long citacionId,
                        @RequestBody Map<String, Object> request) {
                try {
                        LocalDateTime horaAtencion = null;
                        
                        // Extraer hora de atención del request si está presente
                        if (request.containsKey("hora_atencion")) {
                                String horaStr = (String) request.get("hora_atencion");
                                if (horaStr != null && !horaStr.isEmpty()) {
                                        horaAtencion = LocalDateTime.parse(horaStr);
                                }
                        }
                        
                        CitacionMedica citacionActualizada = citacionMedicaService.completarAtencionMedica(citacionId, horaAtencion);
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Atención médica completada y seguimientos iniciados automáticamente");
                        response.put("citacion", citacionMedicaMapper.aDTO(citacionActualizada));
                        response.put("paciente_id", citacionActualizada.getPacienteId());
                        response.put("campana_id", citacionActualizada.getCampanaId());
                        response.put("hora_atencion_final", citacionActualizada.getHoraAtencion());
                        response.put("timestamp", LocalDateTime.now());
                        
                        return ResponseEntity.ok(response);
                        
                } catch (Exception e) {
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("success", false);
                        errorResponse.put("message", "Error completando atención médica: " + e.getMessage());
                        errorResponse.put("citacion_id", citacionId);
                        
                        return ResponseEntity.badRequest().body(errorResponse);
                }
        }

        /**
         * 🔄 NUEVO: Actualizar estado de citación
         */
        @PutMapping("/{citacionId}/estado")
        @Operation(summary = "Actualizar estado de citación", 
                   description = "Actualiza el estado de una citación (AGENDADA, ATENDIDA, CANCELADA)")
        public ResponseEntity<Map<String, Object>> actualizarEstado(
                        @PathVariable Long citacionId,
                        @RequestBody Map<String, Object> request) {
                try {
                        String estadoStr = (String) request.get("estado");
                        if (estadoStr == null || estadoStr.isEmpty()) {
                                throw new IllegalArgumentException("El estado es requerido");
                        }
                        
                        EstadoCitacion nuevoEstado;
                        try {
                                nuevoEstado = EstadoCitacion.valueOf(estadoStr.toUpperCase());
                        } catch (IllegalArgumentException e) {
                                throw new IllegalArgumentException("Estado inválido. Estados válidos: AGENDADA, ATENDIDA, CANCELADA");
                        }
                        
                        CitacionMedica citacionActualizada = citacionMedicaService.actualizarEstadoCitacion(citacionId, nuevoEstado);
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Estado de citación actualizado correctamente");
                        response.put("citacion", citacionMedicaMapper.aDTO(citacionActualizada));
                        response.put("estado_anterior", request.get("estado_anterior"));
                        response.put("estado_nuevo", nuevoEstado);
                        response.put("seguimientos_activados", nuevoEstado == EstadoCitacion.ATENDIDA);
                        response.put("timestamp", LocalDateTime.now());
                        
                        return ResponseEntity.ok(response);
                        
                } catch (Exception e) {
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("success", false);
                        errorResponse.put("message", "Error actualizando estado: " + e.getMessage());
                        errorResponse.put("citacion_id", citacionId);
                        
                        return ResponseEntity.badRequest().body(errorResponse);
                }
        }

}