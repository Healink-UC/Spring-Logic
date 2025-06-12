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

    @GetMapping("/citacion/{citacion_id}")
    @Operation(summary = "Buscar seguimientos por citación médica", description = "Obtiene todos los seguimientos de una citación médica específica")
    public ResponseEntity<List<SeguimientoDTO>> buscarPorCitacionId(@PathVariable Long citacion_id) {
        List<Seguimiento> seguimientos = seguimientoService.buscarPorCitacionId(citacion_id);
        return ResponseEntity.ok(mapeador.aListaDTO(seguimientos));
    }

    @GetMapping("/citacion/{citacion_id}/paginado")
    @Operation(summary = "Buscar seguimientos por citación médica (paginado)", description = "Obtiene todos los seguimientos de una citación médica específica paginada")
    public ResponseEntity<Page<SeguimientoDTO>> buscarPorCitacionIdPaginado(
            @PathVariable Long citacion_id, Pageable pageable) {
        Page<Seguimiento> pagina = seguimientoService.buscarPorCitacionId(citacion_id, pageable);
        return ResponseEntity.ok(pagina.map(seguimiento -> mapeador.aDTO(seguimiento)));
    }
}