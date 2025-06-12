// package com.healink.integrador.domain.atenciones_medicas;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.healink.integrador.core.controller.ControladorGenerico;
// import com.healink.integrador.domain.citaciones_medicas.CitacionMedica;
// import com.healink.integrador.domain.citaciones_medicas.CitacionMedicaService;
// import com.healink.integrador.domain.citaciones_medicas.EstadoCitacion;

// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.parameters.RequestBody;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.persistence.EntityNotFoundException;
// import lombok.extern.slf4j.Slf4j;

// import java.sql.Timestamp;

// @Slf4j
// @RestController
// @RequestMapping("/api/atenciones-medicas")
// @Tag(name = "Atenciones Médicas", description = "API para gestión de atenciones médicas")
// public class AtencionMedicaController extends ControladorGenerico<AtencionMedica, AtencionMedicaDTO> {

//     private final AtencionMedicaService atencionMedicaService;
//     private final CitacionMedicaService citacionMedicaService;

//     public AtencionMedicaController(AtencionMedicaService atencionMedicaService,
//             AtencionMedicaMapper atencionMedicaMapper, CitacionMedicaService citacionMedicaService) {
//         super(atencionMedicaService, atencionMedicaMapper);
//         this.atencionMedicaService = atencionMedicaService;
//         this.citacionMedicaService = citacionMedicaService;
//     }

//     /**
//      * Actualización parcial de atención médica - Solo actualiza los campos enviados
//      * Sobreescribe el comportamiento del controlador genérico para permitir
//      * actualizaciones parciales
//      */
//     @PutMapping("/{id}")
//     @Operation(summary = "Actualizar atención médica parcialmente", description = "Actualiza solo los campos enviados en el JSON, los demás quedan sin cambios")
//     @Override
//     public ResponseEntity<AtencionMedicaDTO> actualizar(@PathVariable Long id, @RequestBody AtencionMedicaDTO dto) {
//         log.info("Actualizando parcialmente atención médica con ID: {}", id);
//         try {
//             // Establecer explícitamente el ID del DTO
//             dto.setId(id);

//             // Obtener la entidad existente
//             AtencionMedica existingEntity = atencionMedicaService.obtenerPorId(id);
//             log.debug("Atención médica existente recuperada: {}", existingEntity);

//             // Validar fechas después de aplicar actualizaciones si ambas están presentes
//             Timestamp fechaInicio = dto.getFechaHoraInicio() != null ? dto.getFechaHoraInicio()
//                     : existingEntity.getFechaHoraInicio();
//             Timestamp fechaFin = dto.getFechaHoraFin() != null ? dto.getFechaHoraFin()
//                     : existingEntity.getFechaHoraFin();

//             if (fechaInicio != null && fechaFin != null && !fechaInicio.before(fechaFin)) {
//                 throw new RuntimeException("La fecha de inicio debe ser anterior a la fecha de fin");
//             }

//             // Actualizar solo los campos no nulos usando lógica personalizada
//             actualizarCamposNoNulos(dto, existingEntity);
//             log.debug("Atención médica actualizada con datos del DTO");

//             // Guardar la entidad actualizada
//             AtencionMedica updatedEntity = atencionMedicaService.guardar(existingEntity);
//             log.info("Atención médica actualizada exitosamente con ID: {}", updatedEntity.getId());

//             return ResponseEntity.ok(mapeador.aDTO(updatedEntity));
//         } catch (EntityNotFoundException e) {
//             log.warn("Atención médica no encontrada con ID: {}", id);
//             return ResponseEntity.notFound().build();
//         } catch (Exception e) {
//             log.error("Error al actualizar atención médica con ID: {}", id, e);
//             throw e;
//         }
//     }

//     /**
//      * Actualiza solo los campos no nulos del DTO a la entidad existente
//      */
//     private void actualizarCamposNoNulos(AtencionMedicaDTO dto, AtencionMedica entity) {
//         if (dto.getCitacionId() != null) {
//             entity.setCitacionId(dto.getCitacionId());
//         }
//         if (dto.getFechaHoraInicio() != null) {
//             entity.setFechaHoraInicio(dto.getFechaHoraInicio());
//         }
//         if (dto.getFechaHoraFin() != null) {
//             entity.setFechaHoraFin(dto.getFechaHoraFin());
//         }
//         if (dto.getDuracionReal() != null) {
//             entity.setDuracionReal(dto.getDuracionReal());
//         }
//         if (dto.getEstado() != null) {
//             entity.setEstado(EstadoAtencionMedica.valueOf(dto.getEstado()));
//         }
//     }

//     @Override
//     @PostMapping()
//     @Operation(summary = "Crear una nueva atención médica", description = "Crea una nueva atención médica con los datos proporcionados")
//     public ResponseEntity<AtencionMedicaDTO> crear(@RequestBody AtencionMedicaDTO atencionMedicaDTO) {

//         // Validar fechas si ambas están presentes
//         if (atencionMedicaDTO.getFechaHoraInicio() != null && atencionMedicaDTO.getFechaHoraFin() != null) {
//             if (!atencionMedicaDTO.getFechaHoraInicio().before(atencionMedicaDTO.getFechaHoraFin())) {
//                 throw new RuntimeException("La fecha de inicio debe ser anterior a la fecha de fin");
//             }
//         }

//         CitacionMedica citacionMedica = citacionMedicaService.obtenerPorId(atencionMedicaDTO.getCitacionId());
//         if (citacionMedica == null) {
//             return ResponseEntity.notFound().build();
//         }

//         if (citacionMedica.getEstado() != EstadoCitacion.AGENDADA) {
//             throw new RuntimeException("La citación no está agendada o ya se ha atendido");
//         }

//         AtencionMedica atencionMedica = mapeador.aEntidad(atencionMedicaDTO);
//         AtencionMedica atencionGuardada = atencionMedicaService.guardar(atencionMedica);

//         citacionMedica.setEstado(EstadoCitacion.ATENDIDA);
//         citacionMedicaService.guardar(citacionMedica);

//         return ResponseEntity.ok(mapeador.aDTO(atencionGuardada));
//     }

//     @GetMapping("/citacion/{citacion_id}")
//     @Operation(summary = "Buscar atención médica por citación", description = "Obtiene la atención médica asociada a una citación específica")
//     public ResponseEntity<AtencionMedicaDTO> buscarPorCitacionId(@PathVariable Long citacion_id) {
//         return atencionMedicaService.buscarPorCitacionId(citacion_id)
//                 .map(atencion -> ResponseEntity.ok(mapeador.aDTO(atencion)))
//                 .orElse(ResponseEntity.notFound().build());
//     }
// }