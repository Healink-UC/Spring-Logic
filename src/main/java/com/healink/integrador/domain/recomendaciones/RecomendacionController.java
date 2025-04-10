package com.healink.integrador.domain.recomendaciones;

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
@RequestMapping("/api/recomendaciones")
@Tag(name = "Recomendaciones", description = "API para gestión de recomendaciones")
public class RecomendacionController extends ControladorGenerico<Recomendacion, RecomendacionDTO> {

    private final RecomendacionService recomendacionService;

    public RecomendacionController(RecomendacionService recomendacionService, RecomendacionMapper recomendacionMapper) {
        super(recomendacionService, recomendacionMapper);
        this.recomendacionService = recomendacionService;
    }

    @GetMapping("/diagnostico/{diagnostico_id}")
    @Operation(summary = "Buscar recomendaciones por diagnóstico", description = "Obtiene todas las recomendaciones de un diagnóstico específico")
    public ResponseEntity<List<RecomendacionDTO>> buscarPorDiagnosticoId(@PathVariable Long diagnosticoId) {
        List<Recomendacion> recomendaciones = recomendacionService.buscarPorDiagnosticoId(diagnosticoId);
        return ResponseEntity.ok(mapeador.aListaDTO(recomendaciones));
    }

    @GetMapping("/diagnostico/{diagnostico_id}/paginado")
    @Operation(summary = "Buscar recomendaciones por diagnóstico (paginado)", description = "Obtiene todas las recomendaciones de un diagnóstico específico de forma paginada")
    public ResponseEntity<Page<RecomendacionDTO>> buscarPorDiagnosticoIdPaginado(
            @PathVariable Long diagnosticoId, Pageable pageable) {
        Page<Recomendacion> pagina = recomendacionService.buscarPorDiagnosticoId(diagnosticoId, pageable);
        return ResponseEntity.ok(pagina.map(recomendacion -> mapeador.aDTO(recomendacion)));
    }
}