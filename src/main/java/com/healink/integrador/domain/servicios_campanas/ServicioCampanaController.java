package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.controller.ControladorGenerico;
// import com.healink.integrador.domain.entidades_salud.EntidadSaludDTO;
// import com.healink.integrador.domain.servicios_medicos.ServicioMedico;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/servicios_campana")
@Tag(name = "ServiciosCampana", description = "API para gestión de servicios campana")
public class ServicioCampanaController extends ControladorGenerico<ServicioCampana, ServicioCampanaDTO> {

    private final ServicioCampanaService servicioCampanaService;

    public ServicioCampanaController(ServicioCampanaService servicioCampanaService, ServicioCampanaMapper servicioCampanaMapper) {
        super(servicioCampanaService, servicioCampanaMapper);
        this.servicioCampanaService = servicioCampanaService;
    }

    @GetMapping("/servicio_campana/servicio/{servicioId}")
    public ResponseEntity<List<ServicioCampanaDTO>> getByServicioId(@PathVariable Long servicioId) {
        return servicioCampanaService.findByServicioId(servicioId)
                .map(servicios_campana -> ResponseEntity.ok(
                        servicios_campana.stream()
                                .map(servicio_campana -> mapeador.aDTO(servicio_campana))
                                .collect(Collectors.toList())
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/servicio_campana/campana/{campanaId}")
    public ResponseEntity<List<ServicioCampanaDTO>> getByCampanaId(@PathVariable Long campanaId) {
        return servicioCampanaService.findByCampanaId(campanaId)
                .map(servicios_campana -> ResponseEntity.ok(
                        servicios_campana.stream()
                                .map(servicio_campana -> mapeador.aDTO(servicio_campana))
                                .collect(Collectors.toList())
                ))
                .orElse(ResponseEntity.notFound().build());
    }
}
