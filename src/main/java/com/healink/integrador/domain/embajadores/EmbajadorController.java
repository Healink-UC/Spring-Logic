package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.controller.ControladorGenerico;
import com.healink.integrador.core.mapper.MapeadorGenerico;
import com.healink.integrador.core.service.ServicioGenerico;
import com.healink.integrador.domain.entidades_salud.EntidadSaludDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/embajadores")
@Tag(name = "Embajdores", description = "API para gestión de embajadores de salud")
public class EmbajadorController extends ControladorGenerico<Embajador, EmbajadorDTO> {

    private EmbajadorService  embajadorService;

    public EmbajadorController(EmbajadorService embajadorService, EmbajadorMapper embajadorMapper) {
        super(embajadorService, embajadorMapper);
        this.embajadorService = embajadorService;
    }

    @GetMapping("/embajador/{usuarioId}")
    public ResponseEntity<EmbajadorDTO> getByUsuario(@PathVariable Long usuarioId) {
        return embajadorService.findByUsuario(usuarioId)
                .map(embajador -> ResponseEntity.ok(mapeador.aDTO(embajador)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/embajador/{entidadId}")
    public ResponseEntity<List<EmbajadorDTO>> getByUsuarioId(@PathVariable Long entidadId) {
        return embajadorService.findByEntidad(entidadId)
                .map(embajadores -> ResponseEntity.ok(
                        embajadores.stream()
                                .map(embajador -> mapeador.aDTO(embajador))
                                .collect(Collectors.toList())
                ))
                .orElse(ResponseEntity.notFound().build());
    }

}
