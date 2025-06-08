package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.controller.ControladorGenerico;
import com.healink.integrador.domain.usuario.Usuario;
import com.healink.integrador.domain.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Embajadores", description = "API para gestión de embajadores de salud")
public class EmbajadorController extends ControladorGenerico<Embajador, EmbajadorDTO> {

    private final EmbajadorService embajadorService;
    private final UsuarioService usuarioService;

    public EmbajadorController(EmbajadorService embajadorService, EmbajadorMapper embajadorMapper, UsuarioService usuarioService) {
        super(embajadorService, embajadorMapper);
        this.embajadorService = embajadorService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EmbajadorDTO> getByUsuario(@PathVariable Long usuarioId) {
        return embajadorService.findByUsuario(usuarioId)
                .map(embajador -> ResponseEntity.ok(mapeador.aDTO(embajador)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entidad/{entidadId}")
    @Operation(summary = "Buscar embajadores por entidad", description = "Obtiene todos los embajadores de una entidad específica")
    public ResponseEntity<List<EmbajadorDTO>> getByEntidad(@PathVariable Long entidadId) {
        return embajadorService.findByEntidad(entidadId)
                .map(embajadores -> ResponseEntity.ok(
                        embajadores.stream()
                                .map(embajador -> {
                                    EmbajadorDTO dto = mapeador.aDTO(embajador);
                                    try {
                                        Usuario usuario = usuarioService.obtenerPorId(embajador.getUsuarioId());
                                        dto.setIdentificacion(usuario.getIdentificacion());
                                        dto.setCorreo(usuario.getCorreo());
                                    } catch (Exception e) {
                                        // Si no se encuentra el usuario, continuamos sin esa información
                                    }
                                    return dto;
                                })
                                .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

}
