package com.healink.integrador.domain.predicciones;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/predicciones")
@Tag(name = "Predicciones")
public class PrediccionController extends ControladorGenerico<Prediccion, PrediccionDTO> {

    private final PrediccionService prediccionService;

    public PrediccionController(PrediccionService prediccionService, PrediccionMapper prediccionMapper) {
        super(prediccionService, prediccionMapper);
        this.prediccionService = prediccionService;
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Buscar predicciones por paciente", description = "Obtiene las predicciones de riesgo cardiaco de un paciente")
    public ResponseEntity<List<PrediccionDTO>> buscarPorPacienteId(@PathVariable Long pacienteId) {
        return prediccionService.buscarPorPacienteId(pacienteId)
                .map(predicciones -> ResponseEntity.ok(mapeador.aListaDTO(predicciones)))
                .orElse(ResponseEntity.notFound().build());
    }

}
