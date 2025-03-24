package com.healink.integrador.domain.historia_clinica;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.core.controller.ControladorGenerico;

import java.util.List;

@RestController
@RequestMapping("/api/historias-clinicas")
@Tag(name = "Historias Clínicas", description = "API para gestión de historias clínicas de pacientes")
public class HistoriaClinicaController extends ControladorGenerico<HistoriaClinica, HistoriaClinicaDTO> {

    private final HistoriaClinicaService historiaClinicaService;

    public HistoriaClinicaController(HistoriaClinicaService historiaClinicaService,
            HistoriaClinicaMapper historiaClinicaMapper) {
        super(historiaClinicaService, historiaClinicaMapper);
        this.historiaClinicaService = historiaClinicaService;
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Buscar historias clínicas por paciente", description = "Obtiene todas las historias clínicas de un paciente específico")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorPacienteId(@PathVariable Long pacienteId) {
        List<HistoriaClinica> historias = historiaClinicaService.buscarPorPacienteId(pacienteId);
        return ResponseEntity.ok(mapeador.aListaDTO(historias));
    }

    @GetMapping("/paciente/{pacienteId}/paginado")
    @Operation(summary = "Buscar historias clínicas por paciente (paginado)", description = "Obtiene las historias clínicas de un paciente de forma paginada")
    public ResponseEntity<Page<HistoriaClinicaDTO>> buscarPorPacienteIdPaginado(
            @PathVariable Long pacienteId, Pageable pageable) {
        Page<HistoriaClinica> pagina = historiaClinicaService.buscarPorPacienteId(pacienteId, pageable);
        return ResponseEntity.ok(pagina.map(historia -> mapeador.aDTO(historia)));
    }

    @GetMapping("/citacion/{citacionId}")
    @Operation(summary = "Buscar historia clínica por citación", description = "Obtiene la historia clínica asociada a una citación específica")
    public ResponseEntity<HistoriaClinicaDTO> buscarPorCitacionId(@PathVariable Long citacionId) {
        return historiaClinicaService.buscarPorCitacionId(citacionId)
                .map(historia -> ResponseEntity.ok(mapeador.aDTO(historia)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{pacienteId}/triaje/{triajeId}")
    @Operation(summary = "Buscar historia clínica por paciente y triaje", description = "Obtiene la historia clínica asociada a un paciente y triaje específicos")
    public ResponseEntity<HistoriaClinicaDTO> buscarPorPacienteYTriajeId(
            @PathVariable Long pacienteId, @PathVariable Long triajeId) {
        return historiaClinicaService.buscarPorPacienteYTriajeId(pacienteId, triajeId)
                .map(historia -> ResponseEntity.ok(mapeador.aDTO(historia)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/riesgo-rehospitalizacion")
    @Operation(summary = "Buscar historias clínicas por riesgo de rehospitalización", description = "Obtiene las historias clínicas con riesgo de rehospitalización superior al umbral especificado")
    public ResponseEntity<List<HistoriaClinicaDTO>> buscarPorRiesgoRehospitalizacion(
            @RequestParam Double umbral) {
        List<HistoriaClinica> historias = historiaClinicaService.buscarPorRiesgoRehospitalizacion(umbral);
        return ResponseEntity.ok(mapeador.aListaDTO(historias));
    }
}