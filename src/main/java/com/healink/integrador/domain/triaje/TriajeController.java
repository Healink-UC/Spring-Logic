package com.healink.integrador.domain.triaje;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/triaje")
@Tag(name = "Triaje", description = "API para gestión de triajes")
public class TriajeController extends ControladorGenerico<Triaje, TriajeDTO> {

    private final TriajeService triajeService;
    private final TriajeMapper triajeMapper;

    public TriajeController(TriajeService triajeService, TriajeMapper triajeMapper) {
        super(triajeService, triajeMapper);
        this.triajeService = triajeService;
        this.triajeMapper = triajeMapper;
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Obtener triajes por ID de paciente", 
              description = "Retorna todos los triajes asociados a un paciente específico")
    public ResponseEntity<List<TriajeDTO>> getByPacienteId(@PathVariable Long pacienteId) {
        List<Triaje> triajes = triajeService.findByPacienteId(pacienteId);
        return ResponseEntity.ok(triajeMapper.aListaDTO(triajes));
    }
}