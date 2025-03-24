package com.healink.integrador.domain.personal_medico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import java.util.List;

@RestController
@RequestMapping("/api/personal-medico")
@Tag(name = "Personal Médico", description = "API para gestión de personal médico")
public class PersonalMedicoController extends ControladorGenerico<PersonalMedico, PersonalMedicoDTO> {

    private final PersonalMedicoService personalMedicoService;

    public PersonalMedicoController(PersonalMedicoService personalMedicoService,
            PersonalMedicoMapper personalMedicoMapper) {
        super(personalMedicoService, personalMedicoMapper);
        this.personalMedicoService = personalMedicoService;
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar médico por usuario", description = "Obtiene un médico por su ID de usuario asociado")
    public ResponseEntity<PersonalMedicoDTO> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return personalMedicoService.buscarPorUsuarioId(usuarioId)
                .map(medico -> ResponseEntity.ok(mapeador.aDTO(medico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entidad/{entidadId}")
    @Operation(summary = "Buscar médicos por entidad", description = "Obtiene todos los médicos de una entidad de salud específica")
    public ResponseEntity<List<PersonalMedicoDTO>> buscarPorEntidadId(@PathVariable Long entidadId) {
        List<PersonalMedico> medicos = personalMedicoService.buscarPorEntidadId(entidadId);
        return ResponseEntity.ok(mapeador.aListaDTO(medicos));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar médicos por estado", description = "Obtiene todos los médicos con un estado específico")
    public ResponseEntity<List<PersonalMedicoDTO>> buscarPorEstado(@PathVariable String estado) {
        List<PersonalMedico> medicos = personalMedicoService.buscarPorEstado(estado);
        return ResponseEntity.ok(mapeador.aListaDTO(medicos));
    }

    @GetMapping("/especialidad/{especialidad}")
    @Operation(summary = "Buscar médicos por especialidad", description = "Obtiene todos los médicos de una especialidad específica")
    public ResponseEntity<List<PersonalMedicoDTO>> buscarPorEspecialidad(@PathVariable String especialidad) {
        List<PersonalMedico> medicos = personalMedicoService.buscarPorEspecialidad(especialidad);
        return ResponseEntity.ok(mapeador.aListaDTO(medicos));
    }
}