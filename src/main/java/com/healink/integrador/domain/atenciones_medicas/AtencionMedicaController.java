package com.healink.integrador.domain.atenciones_medicas;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/atenciones_medicas")
@Tag(name = "Atenciones Médicas", description = "API para gestión de atenciones médicas")
public class AtencionMedicaController extends ControladorGenerico<AtencionMedica, AtencionMedicaDTO> {

    private final AtencionMedicaService atencionMedicaService;

    public AtencionMedicaController(AtencionMedicaService atencionMedicaService, AtencionMedicaMapper atencionMedicaMapper) {
        super(atencionMedicaService, atencionMedicaMapper);
        this.atencionMedicaService = atencionMedicaService;
    }

    @GetMapping("/citacion/{citacion_id}")
    @Operation(summary = "Buscar atención médica por citación", description = "Obtiene la atención médica asociada a una citación específica")
    public ResponseEntity<AtencionMedicaDTO> buscarPorCitacionId(@PathVariable Long citacion_id) {
        return atencionMedicaService.buscarPorCitacionId(citacion_id)
                .map(atencion -> ResponseEntity.ok(mapeador.aDTO(atencion)))
                .orElse(ResponseEntity.notFound().build());
    }
}