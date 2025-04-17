package com.healink.integrador.domain.localizacion;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/localizaciones")
@Tag(name = "Localizaciones", description = "API para gestión de localizaciones")
public class LocalizacionController extends ControladorGenerico<Localizacion, LocalizacionDTO> {

    private final LocalizacionService localizacionService;

    public LocalizacionController(LocalizacionService localizacionService, LocalizacionMapper localizacionMapper) {
        super(localizacionService, localizacionMapper);
        this.localizacionService = localizacionService;
    }

    @GetMapping("/localizaciones/{localizacionId}")
    public ResponseEntity<LocalizacionDTO> getById(@PathVariable Long localizacionId) {
        return localizacionService.buscarPorId(localizacionId)
                .map(localizacion -> ResponseEntity.ok(mapeador.aDTO(localizacion)))
                .orElse(ResponseEntity.notFound().build());
    }

}
