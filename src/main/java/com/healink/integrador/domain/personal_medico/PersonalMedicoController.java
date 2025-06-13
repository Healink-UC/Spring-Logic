package com.healink.integrador.domain.personal_medico;

import com.healink.integrador.core.controller.ControladorGenerico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personal-medico")
@Tag(name = "Personal Médico", description = "API para gestión del personal médico")
@Slf4j
public class PersonalMedicoController extends ControladorGenerico<PersonalMedico, PersonalMedicoDTO> {

    private final PersonalMedicoService personalMedicoService;

    public PersonalMedicoController(PersonalMedicoService personalMedicoService, PersonalMedicoMapper personalMedicoMapper) {
        super(personalMedicoService, personalMedicoMapper);
        this.personalMedicoService = personalMedicoService;
    }

    /**
     * Buscar personal médico por ID con relaciones lazy cargadas
     * Este endpoint soluciona el LazyInitializationException
     */
    @GetMapping("/{id}/details")
    @Operation(summary = "Buscar personal médico por ID con detalles completos")
    public ResponseEntity<PersonalMedicoDTO> getByIdWithDetails(@PathVariable Long id) {
        log.info("Buscando personal médico por ID con detalles: {}", id);
        return personalMedicoService.findByIdWithRelations(id)
                .map(personalMedico -> ResponseEntity.ok(mapeador.aDTO(personalMedico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar personal médico por ID de usuario")
    public ResponseEntity<PersonalMedicoDTO> getByUsuario(@PathVariable Long usuarioId) {
        log.info("Buscando personal médico por usuario ID: {}", usuarioId);
        return personalMedicoService.findByUsuario(usuarioId)
                .map(personalMedico -> ResponseEntity.ok(mapeador.aDTO(personalMedico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entidad/{entidadId}")
    @Operation(summary = "Buscar personal médico por ID de entidad de salud")
    public ResponseEntity<List<PersonalMedicoDTO>> getByEntidad(@PathVariable Long entidadId) {
        log.info("Buscando personal médico por entidad ID: {}", entidadId);
        List<PersonalMedico> personalMedico = personalMedicoService.findByEntidad(entidadId);
        List<PersonalMedicoDTO> dtos = personalMedico.stream()
                .map(mapeador::aDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/especialidad/{especialidad}")
    @Operation(summary = "Buscar personal médico por especialidad")
    public ResponseEntity<List<PersonalMedicoDTO>> getByEspecialidad(@PathVariable String especialidad) {
        log.info("Buscando personal médico por especialidad: {}", especialidad);
        List<PersonalMedico> personalMedico = personalMedicoService.findByEspecialidad(especialidad);
        List<PersonalMedicoDTO> dtos = personalMedico.stream()
                .map(mapeador::aDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/entidad/{entidadId}/especialidad/{especialidad}")
    @Operation(summary = "Buscar personal médico por entidad y especialidad")
    public ResponseEntity<List<PersonalMedicoDTO>> getByEntidadAndEspecialidad(
            @PathVariable Long entidadId, 
            @PathVariable String especialidad) {
        log.info("Buscando personal médico por entidad ID: {} y especialidad: {}", entidadId, especialidad);
        List<PersonalMedico> personalMedico = personalMedicoService.findByEntidadAndEspecialidad(entidadId, especialidad);
        List<PersonalMedicoDTO> dtos = personalMedico.stream()
                .map(mapeador::aDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
} 