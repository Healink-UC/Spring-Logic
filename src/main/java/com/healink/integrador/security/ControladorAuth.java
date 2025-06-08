package com.healink.integrador.security;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.healink.integrador.domain.usuario.TipoIdentificacion;
import com.healink.integrador.domain.usuario.Usuario;
import com.healink.integrador.domain.usuario.UsuarioDTO;
import com.healink.integrador.domain.usuario.UsuarioMapper;
import com.healink.integrador.domain.usuario.UsuarioService;

import jakarta.transaction.Transactional;
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
    private final ListaNegraToken listaNegraToken;

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

    @PostMapping("/acceso")
    @Transactional
    public ResponseEntity<?> login(@RequestBody SolicitudAcceso solicitud) {
        try {
            // Autenticar
            TipoIdentificacion tipoId = solicitud.getTipoIdentificacion();
            String credencial = tipoId + ":" + solicitud.getIdentificacion();

            Authentication auth = gestorAutenticacion.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credencial,
                            solicitud.getClave()));

            // Obtener usuario autenticado básico
            Usuario usuarioAuth = (Usuario) auth.getPrincipal();
            
            // Obtener usuario completo con entidad de salud desde la base de datos
            Usuario usuario = usuarioService.findByTipoIdentificacionAndIdentificacion(
                    usuarioAuth.getTipoIdentificacion(),
                    usuarioAuth.getIdentificacion())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Generar token
            String token = proveedorTokenJWT.createToken(usuario);

            // Respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("usuario", usuarioMapper.aDTOConEntidadSalud(usuario));

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error de autenticación: " + e.getMessage() + " - " + e.getClass().getSimpleName());
        }
    }

    @PostMapping("/salir")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            listaNegraToken.agregarAListaNegra(token);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Sesión cerrada exitosamente");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(
                Collections.singletonMap("error", "Token no proporcionado o formato inválido"));
    }
}