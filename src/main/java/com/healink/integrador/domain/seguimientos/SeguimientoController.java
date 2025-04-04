package com.healink.integrador.domain.seguimientos;

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
@RequestMapping("/api/seguimientos")
@Tag(name = "Seguimientos", description = "API para gestión de seguimientos")
public class SeguimientoController extends ControladorGenerico<Seguimiento, SeguimientoDTO> {

    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService, SeguimientoMapper seguimientoMapper) {
        super(seguimientoService, seguimientoMapper);
        this.seguimientoService = seguimientoService;
    }

    @GetMapping("/atencion/{atencion_id}")
    @Operation(summary = "Buscar seguimientos por atención médica", description = "Obtiene todos los seguimientos de una atención médica específica")
    public ResponseEntity<List<SeguimientoDTO>> buscarPorDiagnosticoId(@PathVariable Long diagnosticoId) {
        List<Seguimiento> seguimientos = seguimientoService.buscarPorAtencionId(diagnosticoId);
        return ResponseEntity.ok(mapeador.aListaDTO(seguimientos));
    }

    @GetMapping("/atencion/{atencion_id}/paginado")
    @Operation(summary = "Buscar seguimientos por atención médica (paginado)", description = "Obtiene todos los seguimientos de una atención médica específica paginada")
    public ResponseEntity<Page<SeguimientoDTO>> buscarPorDiagnosticoIdPaginado(
            @PathVariable Long diagnosticoId, Pageable pageable) {
        Page<Seguimiento> pagina = seguimientoService.buscarPorAtencionId(diagnosticoId, pageable);
        return ResponseEntity.ok(pagina.map(seguimiento -> mapeador.aDTO(seguimiento)));
    }
}