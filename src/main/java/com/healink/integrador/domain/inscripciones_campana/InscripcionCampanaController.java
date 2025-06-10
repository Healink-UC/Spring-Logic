package com.healink.integrador.domain.inscripciones_campana;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones-campana")
@Tag(name = "Inscripciones a Campañas", description = "API para gestionar inscripciones de usuarios a campañas")
public class InscripcionCampanaController extends ControladorGenerico<InscripcionCampana, InscripcionCampanaDTO> {

    private final InscripcionCampanaService inscripcionCampanaService;
    private final InscripcionCampanaMapper inscripcionCampanaMapper;

    public InscripcionCampanaController(InscripcionCampanaService inscripcionCampanaService,
            InscripcionCampanaMapper inscripcionCampanaMapper) {
        super(inscripcionCampanaService, inscripcionCampanaMapper);
        this.inscripcionCampanaService = inscripcionCampanaService;
        this.inscripcionCampanaMapper = inscripcionCampanaMapper;
    }

    @Override
    @PostMapping
    @Operation(summary = "Crear inscripción a campaña", description = "Crea una nueva inscripción de usuario a campaña")
    public ResponseEntity<InscripcionCampanaDTO> crear(@Valid @RequestBody InscripcionCampanaDTO dto) {
        InscripcionCampana entidad = inscripcionCampanaMapper.aEntidad(dto);
        InscripcionCampana inscripcionCreada = inscripcionCampanaService.crearInscripcion(entidad);
        return ResponseEntity.ok(inscripcionCampanaMapper.aDTO(inscripcionCreada));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener inscripciones por ID de usuario", description = "Retorna todas las inscripciones de un usuario específico")
    public ResponseEntity<List<InscripcionCampanaDTO>> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        List<InscripcionCampana> inscripciones = inscripcionCampanaService.buscarPorUsuarioId(usuarioId);
        return ResponseEntity.ok(inscripcionCampanaMapper.aListaDTO(inscripciones));
    }

    @GetMapping("/usuario/{usuarioId}/activas")
    @Operation(summary = "Obtener inscripciones activas por ID de usuario", description = "Retorna las inscripciones activas de un usuario específico")
    public ResponseEntity<List<InscripcionCampanaDTO>> obtenerInscripcionesActivas(@PathVariable Long usuarioId) {
        List<InscripcionCampana> inscripciones = inscripcionCampanaService.buscarInscripcionesActivas(usuarioId);
        return ResponseEntity.ok(inscripcionCampanaMapper.aListaDTO(inscripciones));
    }

    @GetMapping("/campana/{campanaId}")
    @Operation(summary = "Obtener inscripciones por ID de campaña", description = "Retorna todas las inscripciones de una campaña específica")
    public ResponseEntity<List<InscripcionCampanaDTO>> obtenerPorCampanaId(@PathVariable Long campanaId) {
        List<InscripcionCampana> inscripciones = inscripcionCampanaService.buscarPorCampanaId(campanaId);
        return ResponseEntity.ok(inscripcionCampanaMapper.aListaDTO(inscripciones));
    }

    @PutMapping("/{id}/retirar")
    @Operation(summary = "Retirar inscripción", description = "Marca una inscripción como retirada y registra el motivo")
    public ResponseEntity<InscripcionCampanaDTO> retirarInscripcion(
            @PathVariable Long id,
            @RequestParam String motivo) {
        InscripcionCampana inscripcion = inscripcionCampanaService.retirarInscripcion(id, motivo);
        return ResponseEntity.ok(inscripcionCampanaMapper.aDTO(inscripcion));
    }
}