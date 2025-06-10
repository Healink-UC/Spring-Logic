package com.healink.integrador.domain.atenciones_medicas;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;
import com.healink.integrador.core.integrations.N8nIntegrationService;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedica;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedicaRepository;
import com.healink.integrador.domain.citaciones_medicas.CitacionMedicaService;
import com.healink.integrador.domain.citaciones_medicas.EstadoCitacion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/atenciones_medicas")
@Tag(name = "Atenciones Médicas", description = "API para gestión de atenciones médicas")
public class AtencionMedicaController extends ControladorGenerico<AtencionMedica, AtencionMedicaDTO> {

    private final AtencionMedicaService atencionMedicaService;
    private final CitacionMedicaService citacionMedicaService;
    private final N8nIntegrationService n8nIntegrationService;

    public AtencionMedicaController(AtencionMedicaService atencionMedicaService, 
                                  AtencionMedicaMapper atencionMedicaMapper, 
                                  CitacionMedicaService citacionMedicaService,
                                  N8nIntegrationService n8nIntegrationService) {
        super(atencionMedicaService, atencionMedicaMapper);
        this.atencionMedicaService = atencionMedicaService;
        this.citacionMedicaService = citacionMedicaService;
        this.n8nIntegrationService = n8nIntegrationService;
    }

    @Override
    @PostMapping("/crear")
    @Operation(summary = "Crear una nueva atención médica", description = "Crea una nueva atención médica con los datos proporcionados")
    public ResponseEntity<AtencionMedicaDTO> crear(@RequestBody AtencionMedicaDTO atencionMedicaDTO) {
        
        CitacionMedica citacionMedica = citacionMedicaService.obtenerPorId(atencionMedicaDTO.getCitacionId());
        if (citacionMedica == null) {
            return ResponseEntity.notFound().build();
        }

        if (citacionMedica.getEstado() != EstadoCitacion.AGENDADA) {
            throw new RuntimeException("La citación no está agendada o ya se ha atendido");
        }

        AtencionMedica atencionMedica = mapeador.aEntidad(atencionMedicaDTO);
        AtencionMedica atencionGuardada = atencionMedicaService.guardar(atencionMedica);

        citacionMedica.setEstado(EstadoCitacion.ATENDIDA);
        citacionMedicaService.guardar(citacionMedica);

        // *** INTEGRACIÓN N8N: Iniciar seguimientos automáticos ***
        try {
            n8nIntegrationService.iniciarSeguimientosPaciente(atencionGuardada);
        } catch (Exception e) {
            // Log del error pero no falla la atención médica
            System.err.println("Error iniciando seguimientos n8n: " + e.getMessage());
        }

        return ResponseEntity.ok(mapeador.aDTO(atencionGuardada));
    }

    @GetMapping("/citacion/{citacion_id}")
    @Operation(summary = "Buscar atención médica por citación", description = "Obtiene la atención médica asociada a una citación específica")
    public ResponseEntity<AtencionMedicaDTO> buscarPorCitacionId(@PathVariable Long citacion_id) {
        return atencionMedicaService.buscarPorCitacionId(citacion_id)
                .map(atencion -> ResponseEntity.ok(mapeador.aDTO(atencion)))
                .orElse(ResponseEntity.notFound().build());
    }
}