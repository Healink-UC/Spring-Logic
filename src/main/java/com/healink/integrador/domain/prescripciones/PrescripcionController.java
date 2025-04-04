package com.healink.integrador.domain.prescripciones;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/prescripciones")
@Tag(name = "Prescripciones", description = "API para gestión de prescripciones")
public class PrescripcionController extends ControladorGenerico<Prescripcion, PrescripcionDTO> {

    private final PrescripcionService prescripcionService;

    public PrescripcionController(PrescripcionService prescripcionService, PrescripcionMapper prescripcionMapper) {
        super(prescripcionService, prescripcionMapper);
        this.prescripcionService = prescripcionService;
    }

    @GetMapping("/diagnostico/{diagnostico_id}")
    @Operation(summary = "Buscar prescripciones por diagnóstico", description = "Obtiene todas las prescripciones de un diagnóstico específico")
    public ResponseEntity<List<PrescripcionDTO>> buscarPorDiagnosticoId(@PathVariable Long diagnosticoId) {
        List<Prescripcion> prescripciones = prescripcionService.buscarPorDiagnosticoId(diagnosticoId);
        return ResponseEntity.ok(mapeador.aListaDTO(prescripciones));
    }

    @GetMapping("/diagnostico/{diagnostico_id}/paginado")
    @Operation(summary = "Buscar prescripciones por diagnóstico (paginado)", description = "Obtiene todas las prescripciones de un diagnóstico específico de forma paginada")
    public ResponseEntity<Page<PrescripcionDTO>> buscarPorDiagnosticoIdPaginado(
            @PathVariable Long diagnosticoId, Pageable pageable) {
        Page<Prescripcion> pagina = prescripcionService.buscarPorDiagnosticoId(diagnosticoId, pageable);
        return ResponseEntity.ok(pagina.map(prescripcion -> mapeador.aDTO(prescripcion)));
    }
}