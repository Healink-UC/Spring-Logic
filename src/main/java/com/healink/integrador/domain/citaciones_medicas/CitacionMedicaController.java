package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.controller.ControladorGenerico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        /**
         * Actualización parcial de cita médica - Solo actualiza los campos enviados
         * Sobreescribe el comportamiento del controlador genérico para permitir actualizaciones parciales
         */
        @PutMapping("/{id}")
        @Operation(summary = "Actualizar cita médica parcialmente", 
                  description = "Actualiza solo los campos enviados en el JSON, los demás quedan sin cambios")
        @Override
        public ResponseEntity<CitacionMedicaDTO> actualizar(@PathVariable Long id, @RequestBody CitacionMedicaDTO dto) {
                log.info("Actualizando parcialmente cita médica con ID: {}", id);
                try {
                        // Establecer explícitamente el ID del DTO
                        dto.setId(id);

                        // Obtener la entidad existente
                        CitacionMedica existingEntity = citacionMedicaService.obtenerPorId(id);
                        log.debug("Cita médica existente recuperada: {}", existingEntity);

                        // Actualizar solo los campos no nulos usando lógica personalizada
                        actualizarCamposNoNulos(dto, existingEntity);
                        log.debug("Cita médica actualizada con datos del DTO");

                        // Guardar la entidad actualizada
                        CitacionMedica updatedEntity = citacionMedicaService.guardar(existingEntity);
                        log.info("Cita médica actualizada exitosamente con ID: {}", updatedEntity.getId());

                        return ResponseEntity.ok(mapeador.aDTO(updatedEntity));
                } catch (EntityNotFoundException e) {
                        log.warn("Cita médica no encontrada con ID: {}", id);
                        return ResponseEntity.notFound().build();
                } catch (Exception e) {
                        log.error("Error al actualizar cita médica con ID: {}", id, e);
                        throw e;
                }
        }

        /**
         * Actualiza solo los campos no nulos del DTO a la entidad existente
         */
        private void actualizarCamposNoNulos(CitacionMedicaDTO dto, CitacionMedica entity) {
                if (dto.getPacienteId() != null) {
                        entity.setPacienteId(dto.getPacienteId());
                }
                if (dto.getCampanaId() != null) {
                        entity.setCampanaId(dto.getCampanaId());
                }
                if (dto.getMedicoId() != null) {
                        entity.setMedicoId(dto.getMedicoId());
                }
                if (dto.getHoraProgramada() != null) {
                        entity.setHoraProgramada(dto.getHoraProgramada());
                }
                if (dto.getHoraAtencion() != null) {
                        entity.setHoraAtencion(dto.getHoraAtencion());
                }
                if (dto.getDuracionEstimada() != null) {
                        entity.setDuracionEstimada(dto.getDuracionEstimada());
                }
                if (dto.getEstado() != null) {
                        entity.setEstado(EstadoCitacion.valueOf(dto.getEstado()));
                }
                if (dto.getPrediccionAsistencia() != null) {
                        entity.setPrediccionAsistencia(dto.getPrediccionAsistencia());
                }
                if (dto.getCodigoTicket() != null) {
                        entity.setCodigoTicket(dto.getCodigoTicket());
                }
                if (dto.getNotas() != null) {
                        entity.setNotas(dto.getNotas());
                }
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

}