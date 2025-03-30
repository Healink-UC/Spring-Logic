package com.healink.integrador.security;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.domain.usuario.Usuario;
import com.healink.integrador.domain.usuario.UsuarioDTO;
import com.healink.integrador.domain.usuario.UsuarioMapper;
import com.healink.integrador.domain.usuario.UsuarioService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ControladorAuth {

    private final AuthenticationManager gestorAutenticacion;
    private final ProveedorTokenJWT proveedorTokenJWT;
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/acceso")
    public ResponseEntity<?> login(@RequestBody SolicitudAcceso solicitud) {
        try {
            // Autenticar
            String identificador = solicitud.getTipoIdentificacion() + ":" + solicitud.getIdentificacion();

            Authentication auth = gestorAutenticacion.authenticate(
                    new UsernamePasswordAuthenticationToken(identificador, solicitud.getClave()));

            // Obtener usuario autenticado
            Usuario usuario = (Usuario) auth.getPrincipal();

            // Generar token
            String token = proveedorTokenJWT.createToken(usuario);

            // Respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("usuario", usuarioMapper.aDTO(usuario));

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Convertir DTO a entidad
            Usuario usuario = usuarioMapper.aEntidad(usuarioDTO);

            // Guardar usuario
            usuario = usuarioService.guardar(usuario);

            // Generar token
            String token = proveedorTokenJWT.createToken(usuario);

            // Respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("usuario", usuarioMapper.aDTO(usuario));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}