package com.healink.integrador.domain.embajadores;

import com.healink.integrador.core.controller.ControladorGenerico;
import com.healink.integrador.domain.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/embajadores")
@Tag(name = "Embajadores", description = "API para gestión de embajadores de salud")
public class EmbajadorController extends ControladorGenerico<Embajador, EmbajadorDTO> {

    private final EmbajadorService embajadorService;

    public EmbajadorController(EmbajadorService embajadorService, EmbajadorMapper embajadorMapper, UsuarioService usuarioService) {
        super(embajadorService, embajadorMapper);
        this.embajadorService = embajadorService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EmbajadorDTO> getByUsuario(@PathVariable Long usuarioId) {
        return embajadorService.findByUsuario(usuarioId)
                .map(embajador -> ResponseEntity.ok(mapeador.aDTO(embajador)))
                .orElse(ResponseEntity.notFound().build());
    }   

}
