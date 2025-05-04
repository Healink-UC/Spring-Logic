package com.healink.integrador.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.healink.integrador.domain.rol.Rol;
import com.healink.integrador.domain.usuario.TipoIdentificacion;
import com.healink.integrador.domain.usuario.Usuario;
import com.healink.integrador.domain.usuario.UsuarioDTO;
import com.healink.integrador.domain.usuario.UsuarioMapper;
import com.healink.integrador.domain.usuario.UsuarioService;

@ExtendWith(MockitoExtension.class)
class ControladorAuthTest {

    @Mock
    private AuthenticationManager gestorAutenticacion;

    @Mock
    private ProveedorTokenJWT proveedorTokenJWT;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private ListaNegraToken listaNegraToken;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ControladorAuth controladorAuth;

    private Usuario usuarioMock;
    private UsuarioDTO usuarioDTOMock;
    private Rol rolMock;

    @BeforeEach
    void setup() {
        // Configurar rol mock
        rolMock = new Rol();
        rolMock.setId(1L);
        rolMock.setNombre("ROLE_USER");

        // Configurar usuario mock
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setTipoIdentificacion(TipoIdentificacion.CC);
        usuarioMock.setIdentificacion("1234567890");
        usuarioMock.setNombres("Juan");
        usuarioMock.setApellidos("Pérez");
        usuarioMock.setRol(rolMock);

        // Configurar DTO mock
        usuarioDTOMock = new UsuarioDTO();
        usuarioDTOMock.setTipoIdentificacion(TipoIdentificacion.CC);
        usuarioDTOMock.setIdentificacion("1234567890");
        usuarioDTOMock.setNombres("Juan");
        usuarioDTOMock.setApellidos("Pérez");
        usuarioDTOMock.setClave("password");
    }

    @Test
    void registro_conDatosValidos_debeRetornarCreated() {
        // Arrange
        when(usuarioMapper.aEntidad(usuarioDTOMock)).thenReturn(usuarioMock);
        when(usuarioService.guardar(usuarioMock)).thenReturn(usuarioMock);
        when(proveedorTokenJWT.createToken(usuarioMock)).thenReturn("token-jwt");
        when(usuarioMapper.aDTO(usuarioMock)).thenReturn(usuarioDTOMock);

        // Act
        ResponseEntity<?> respuesta = controladorAuth.registro(usuarioDTOMock);

        // Assert
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertTrue(respuesta.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) respuesta.getBody();
        assertEquals("token-jwt", body.get("token"));
        assertEquals(usuarioDTOMock, body.get("usuario"));

        // Verify
        verify(usuarioMapper).aEntidad(usuarioDTOMock);
        verify(usuarioService).guardar(usuarioMock);
        verify(proveedorTokenJWT).createToken(usuarioMock);
        verify(usuarioMapper).aDTO(usuarioMock);
    }

    @Test
    void registro_conError_debeRetornarBadRequest() {
        // Arrange
        String mensajeError = "Error al guardar usuario";
        when(usuarioMapper.aEntidad(usuarioDTOMock)).thenReturn(usuarioMock);
        when(usuarioService.guardar(usuarioMock)).thenThrow(new RuntimeException(mensajeError));

        // Act
        ResponseEntity<?> respuesta = controladorAuth.registro(usuarioDTOMock);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(mensajeError, respuesta.getBody());

        // Verify
        verify(usuarioMapper).aEntidad(usuarioDTOMock);
        verify(usuarioService).guardar(usuarioMock);
        verify(proveedorTokenJWT, never()).createToken(any());
    }

    @Test
    void acceso_conCredencialesValidas_debeRetornarOk() {
        // Arrange
        // Usamos setters en lugar del constructor
        SolicitudAcceso solicitud = new SolicitudAcceso();
        solicitud.setTipoIdentificacion(TipoIdentificacion.CC);
        solicitud.setIdentificacion("1234567890");
        solicitud.setClave("password");

        String credencial = "CC:1234567890";

        when(gestorAutenticacion.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuarioMock);
        when(proveedorTokenJWT.createToken(usuarioMock)).thenReturn("token-jwt");
        when(usuarioMapper.aDTO(usuarioMock)).thenReturn(usuarioDTOMock);

        // Act
        ResponseEntity<?> respuesta = controladorAuth.login(solicitud);

        // Assert
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) respuesta.getBody();
        assertEquals("token-jwt", body.get("token"));
        assertEquals(usuarioDTOMock, body.get("usuario"));

        // Verify
        verify(gestorAutenticacion).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(proveedorTokenJWT).createToken(usuarioMock);
        verify(usuarioMapper).aDTO(usuarioMock);
    }

    @Test
    void acceso_conCredencialesInvalidas_debeRetornarUnauthorized() {
        // Arrange
        // Usamos setters en lugar del constructor
        SolicitudAcceso solicitud = new SolicitudAcceso();
        solicitud.setTipoIdentificacion(TipoIdentificacion.CC);
        solicitud.setIdentificacion("1234567890");
        solicitud.setClave("wrong-password");

        when(gestorAutenticacion.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // Act
        ResponseEntity<?> respuesta = controladorAuth.login(solicitud);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
        assertTrue(respuesta.getBody() instanceof String);
        assertTrue(((String) respuesta.getBody()).contains("Error de autenticación"));

        // Verify
        verify(gestorAutenticacion).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(proveedorTokenJWT, never()).createToken(any());
        verify(usuarioMapper, never()).aDTO(any());
    }

    @Test
    void salir_conTokenValido_debeRetornarOk() {
        // Arrange
        String authHeader = "Bearer token-jwt";

        // Act
        ResponseEntity<Map<String, String>> respuesta = controladorAuth.logout(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Sesión cerrada exitosamente", respuesta.getBody().get("mensaje"));

        // Verify
        verify(listaNegraToken).agregarAListaNegra("token-jwt");
    }

    @Test
    void salir_sinToken_debeRetornarBadRequest() {
        // Act
        ResponseEntity<Map<String, String>> respuesta = controladorAuth.logout(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Token no proporcionado o formato inválido", respuesta.getBody().get("error"));

        // Verify
        verify(listaNegraToken, never()).agregarAListaNegra(anyString());
    }

    @Test
    void salir_conTokenMalFormateado_debeRetornarBadRequest() {
        // Arrange
        String authHeader = "MalFormato token-jwt";

        // Act
        ResponseEntity<Map<String, String>> respuesta = controladorAuth.logout(authHeader);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Token no proporcionado o formato inválido", respuesta.getBody().get("error"));

        // Verify
        verify(listaNegraToken, never()).agregarAListaNegra(anyString());
    }
}