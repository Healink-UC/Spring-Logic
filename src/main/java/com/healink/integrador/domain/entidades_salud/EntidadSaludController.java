package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.controller.ControladorGenerico;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entidades-salud")
@Tag(name = "EntidadesSalud", description = "API para gestión de entidades de salud")
public class EntidadSaludController extends ControladorGenerico<EntidadSalud, EntidadSaludDTO> {

    private final EntidadSaludService entidadSaludService;

    public EntidadSaludController(EntidadSaludService entidadSaludService, EntidadSaludMapper entidadSaludMapper) {
        super(entidadSaludService, entidadSaludMapper);
        this.entidadSaludService = entidadSaludService;
    }

    @GetMapping("/usuario/{usuario_id}")
    public ResponseEntity<EntidadSaludDTO> getByUsuarioId(@PathVariable Long usuario_id) {
        return entidadSaludService.findByUsuarioId(usuario_id)
                .map(entidad_salud -> ResponseEntity.ok(mapeador.aDTO(entidad_salud)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/razon-social/{razon_social}")
    public ResponseEntity<EntidadSaludDTO> getByRazonSocial(@PathVariable String razon_social) {
        return entidadSaludService.findByRazonSocial(razon_social)
                .map(entidad_salud -> ResponseEntity.ok(mapeador.aDTO(entidad_salud)))
                .orElse(ResponseEntity.notFound().build());
    }

}
