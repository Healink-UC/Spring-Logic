package com.healink.integrador.domain.usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;
import com.healink.integrador.enums.TipoIdentificacion;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
public class UsuarioController extends ControladorGenerico<Usuario, UsuarioDTO> {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        super(usuarioService, usuarioMapper);
        this.usuarioService = usuarioService;
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioDTO> getByCorreo(@PathVariable String correo) {
        return usuarioService.findByCorreo(correo)
                .map(usuario -> ResponseEntity.ok(mapeador.aDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/identificacion/{tipo}/{numero}")
    public ResponseEntity<UsuarioDTO> getByIdentificacion(
            @PathVariable("tipo") TipoIdentificacion tipoIdentificacion,
            @PathVariable("numero") String identificacion) {
        return usuarioService.findByTipoIdentificacionAndIdentificacion(
                tipoIdentificacion, identificacion)
                .map(usuario -> ResponseEntity.ok(mapeador.aDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }
}
