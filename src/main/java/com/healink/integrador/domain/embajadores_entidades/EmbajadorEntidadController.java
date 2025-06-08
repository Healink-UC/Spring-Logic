package com.healink.integrador.domain.embajadores_entidades;

import com.healink.integrador.core.controller.ControladorGenerico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/embajadores-entidades")
@Tag(name = "Embajadores-Entidades", description = "API para gestión de relaciones entre embajadores y entidades")
public class EmbajadorEntidadController extends ControladorGenerico<EmbajadorEntidad, EmbajadorEntidadDTO> {

    private final EmbajadorEntidadService embajadorEntidadService;

    public EmbajadorEntidadController(EmbajadorEntidadService embajadorEntidadService, 
                                    EmbajadorEntidadMapper embajadorEntidadMapper) {
        super(embajadorEntidadService, embajadorEntidadMapper);
        this.embajadorEntidadService = embajadorEntidadService;
    }

    @GetMapping("/entidad/{entidadId}")
    @Operation(summary = "Buscar embajadores por entidad", description = "Obtiene todos los embajadores asociados a una entidad específica")
    public ResponseEntity<List<EmbajadorEntidadDTO>> getByEntidad(@PathVariable Long entidadId) {
        List<EmbajadorEntidad> embajadores = embajadorEntidadService.findByEntidadId(entidadId);
        return ResponseEntity.ok(mapeador.aListaDTO(embajadores));
    }

    @GetMapping("/embajador/{embajadorId}")
    @Operation(summary = "Buscar entidades por embajador", description = "Obtiene todas las entidades asociadas a un embajador específico")
    public ResponseEntity<List<EmbajadorEntidadDTO>> getByEmbajador(@PathVariable Long embajadorId) {
        List<EmbajadorEntidad> entidades = embajadorEntidadService.findByEmbajadorId(embajadorId);
        return ResponseEntity.ok(mapeador.aListaDTO(entidades));
    }

    @PostMapping("/validar")
    @Operation(summary = "Validar relación", description = "Verifica si existe una relación entre un embajador y una entidad")
    public ResponseEntity<Boolean> validarRelacion(
            @RequestParam Long embajadorId,
            @RequestParam Long entidadId) {
        boolean existe = embajadorEntidadService.existsByEmbajadorIdAndEntidadId(embajadorId, entidadId);
        return ResponseEntity.ok(existe);
    }
} 