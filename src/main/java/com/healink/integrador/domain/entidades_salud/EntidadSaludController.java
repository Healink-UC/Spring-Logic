package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.controller.ControladorGenerico;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entidades_salud")
@Tag(name = "EntidadesSalud", description = "API para gestión de entidades de salud")
public class EntidadSaludController extends ControladorGenerico<EntidadSalud, EntidadSaludDTO> {

    private final EntidadSaludService entidadSaludService;

    public EntidadSaludController(EntidadSaludService entidadSaludService, EntidadSaludMapper entidadSaludMapper) {
        super(entidadSaludService, entidadSaludMapper);
        this.entidadSaludService = entidadSaludService;
    }

    @GetMapping("/entidad_salud/{razon_social}")
    public ResponseEntity<EntidadSaludDTO> getByUsuarioId(@PathVariable String razon_social) {
        return entidadSaludService.findByRazonSocial(razon_social)
                .map(entidad_salud -> ResponseEntity.ok(mapeador.aDTO(entidad_salud)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entidad_salud/{usuarioId}")
    public ResponseEntity<EntidadSaludDTO> getByUsuarioId(@PathVariable Long usuarioId) {
        return entidadSaludService.findByUsuarioId(usuarioId)
                .map(entidad_salud -> ResponseEntity.ok(mapeador.aDTO(entidad_salud)))
                .orElse(ResponseEntity.notFound().build());
    }
}
