package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.controller.ControladorGenerico;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/servicios-campana")
@Tag(name = "ServiciosCampana", description = "API para gestión de servicios campana")
public class ServicioCampanaController extends ControladorGenerico<ServicioCampana, ServicioCampanaDTO> {

    private final ServicioCampanaService servicioCampanaService;

    public ServicioCampanaController(ServicioCampanaService servicioCampanaService,
            ServicioCampanaMapper servicioCampanaMapper) {
        super(servicioCampanaService, servicioCampanaMapper);
        this.servicioCampanaService = servicioCampanaService;
    }

    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<List<ServicioCampanaDTO>> getByServicioId(@PathVariable Long servicioId) {
        return servicioCampanaService.findByServicioId(servicioId)
                .map(servicios_campana -> ResponseEntity.ok(
                        servicios_campana.stream()
                                .map(servicio_campana -> mapeador.aDTO(servicio_campana))
                                .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/campana/{campanaId}")
    public ResponseEntity<List<ServicioCampanaDTO>> getByCampanaId(@PathVariable Long campanaId) {
        return servicioCampanaService.findByCampanaId(campanaId)
                .map(servicios_campana -> ResponseEntity.ok(
                        servicios_campana.stream()
                                .map(servicio_campana -> mapeador.aDTO(servicio_campana))
                                .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/campana/{campanaId}/servicios")
    public ResponseEntity<List<ServicioCampanaDTO>> agregarServiciosCampana(
            @PathVariable Long campanaId,
            @RequestBody @Valid List<Long> serviciosIds) {

        List<ServicioCampana> serviciosCampana = servicioCampanaService.agregarServiciosCampana(campanaId,
                serviciosIds);

        List<ServicioCampanaDTO> serviciosCampanaDTO = serviciosCampana.stream()
                .map(servicioCampana -> mapeador.aDTO(servicioCampana))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(serviciosCampanaDTO);
    }
}