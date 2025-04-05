package com.healink.integrador.domain.diagnosticos;

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
@RequestMapping("/api/diagnosticos")
@Tag(name = "Diagnósticos", description = "API para gestión de diagnósticos")
public class DiagnosticoController extends ControladorGenerico<Diagnostico, DiagnosticoDTO> {

    private final DiagnosticoService diagnosticoService;

    public DiagnosticoController(DiagnosticoService diagnosticoService, DiagnosticoMapper diagnosticoMapper) {
        super(diagnosticoService, diagnosticoMapper);
        this.diagnosticoService = diagnosticoService;
    }

    @GetMapping("/atencion/{atencion_id}")
    @Operation(summary = "Buscar diagnósticos por atención médica", description = "Obtiene todas los diagnósticos de una atención médica específica")
    public ResponseEntity<List<DiagnosticoDTO>> buscarPorAtencionId(@PathVariable Long atencionId) {
        List<Diagnostico> diagnosticos = diagnosticoService.buscarPorAtencionId(atencionId);
        return ResponseEntity.ok(mapeador.aListaDTO(diagnosticos));
    }

    @GetMapping("/atencion/{atencion_id}/paginado")
    @Operation(summary = "Buscar diagnósticos por atención médica (paginado)", description = "Obtiene todas los diagnósticos de una atención médica específica de forma paginada")
    public ResponseEntity<Page<DiagnosticoDTO>> buscarPorAtencionIdPaginado(
            @PathVariable Long pacienteId, Pageable pageable) {
        Page<Diagnostico> pagina = diagnosticoService.buscarPorAtencionId(pacienteId, pageable);
        return ResponseEntity.ok(pagina.map(diagnostico -> mapeador.aDTO(diagnostico)));
    }

    @GetMapping("/codigo_cie10/{codigo_cie10}")
    @Operation(summary = "Buscar diagnósticos por código CIE 10", description = "Obtiene todas los diagnósticos que tienen el código CIE 10 especificado")
    public ResponseEntity<List<DiagnosticoDTO>> buscarPorCodigoCie10(@PathVariable String codigo_cie10) {
        List<Diagnostico> diagnosticos = diagnosticoService.buscarPorCodigoCie10(codigo_cie10);
        return ResponseEntity.ok(mapeador.aListaDTO(diagnosticos));
    }

    @GetMapping("/codigo_cie10/{codigo_cie10}/paginado")
    @Operation(summary = "Buscar diagnósticos por código CIE 10 (paginado)", description = "Obtiene todas los diagnósticos que tienen el código CIE 10 especificado de forma paginada")
    public ResponseEntity<Page<DiagnosticoDTO>> buscarPorCodigoCie10Paginado(
            @PathVariable String codigo_cie10, Pageable pageable) {
        Page<Diagnostico> pagina = diagnosticoService.buscarPorCodigoCie10(codigo_cie10, pageable);
        return ResponseEntity.ok(pagina.map(diagnostico -> mapeador.aDTO(diagnostico)));
    }
}