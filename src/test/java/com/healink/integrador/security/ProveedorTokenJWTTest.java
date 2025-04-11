package com.healink.integrador.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.healink.integrador.config.ConfiguracionJWT;
import com.healink.integrador.domain.usuario.Usuario;
import com.healink.integrador.enums.TipoIdentificacion;

@ExtendWith(MockitoExtension.class)
class ProveedorTokenJWTTest {

    @Mock
    private ConfiguracionJWT configJWT;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private ListaNegraToken listaNegraToken;

    @InjectMocks
    private ProveedorTokenJWT proveedorTokenJWT;

    private Usuario usuarioMock;
    private String llaveSecretaMock = "MCvBIKwPaVjhmKBwFUdHDXzvfWeAZgaJ8XJr6cBsvNkPI9UvAWw7GzFMV6ZhHsI";

    @BeforeEach
    void setup() {
        // Configurar el mock de ConfiguracionJWT
        when(configJWT.getLlaveSecreta()).thenReturn(llaveSecretaMock);
        when(configJWT.getExpiracion()).thenReturn(3600L); // 1 hora

        // Crear usuario mock
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setTipoIdentificacion(TipoIdentificacion.CC);
        usuarioMock.setIdentificacion("1234567890");
        usuarioMock.setNombres("Juan");
        usuarioMock.setApellidos("Pérez");
        usuarioMock.setClave("password");
        usuarioMock.setRol(null); // Asigna un rol si es necesario
    }

    @Test
    void createToken_debeGenerarTokenValido() {
        // Act
        String token = proveedorTokenJWT.createToken(usuarioMock);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 20); // Un token JWT válido debe tener cierta longitud

        // Verificamos que se usó la configuración
        verify(configJWT).getLlaveSecreta();
        verify(configJWT).getExpiracion();
    }

    @Test
    void getUsername_debeExtraerUsernameCorrectamente() {
        // Arrange
        String token = proveedorTokenJWT.createToken(usuarioMock);

        // Act
        String username = proveedorTokenJWT.getUsername(token);

        // Assert
        assertEquals(usuarioMock.getUsername(), username);
    }

    @Test
    void validateToken_conTokenValido_debeRetornarTrue() {
        // Arrange
        String token = proveedorTokenJWT.createToken(usuarioMock);
        when(listaNegraToken.estaEnListaNegra(token)).thenReturn(false);

        // Act
        boolean isValid = proveedorTokenJWT.validateToken(token);

        // Assert
        assertTrue(isValid);
        verify(listaNegraToken).estaEnListaNegra(token);
    }

    @Test
    void validateToken_conTokenEnListaNegra_debeRetornarFalse() {
        // Arrange
        String token = proveedorTokenJWT.createToken(usuarioMock);
        when(listaNegraToken.estaEnListaNegra(token)).thenReturn(true);

        // Act
        boolean isValid = proveedorTokenJWT.validateToken(token);

        // Assert
        assertFalse(isValid);
        verify(listaNegraToken).estaEnListaNegra(token);
    }

    @Test
    void getAuthentication_debeRetornarAuthenticationCorrectamente() {
        // Arrange
        String token = proveedorTokenJWT.createToken(usuarioMock);

        // Mock de UserDetailsService
        when(userDetailsService.loadUserByUsername(usuarioMock.getUsername())).thenReturn(usuarioMock);

        // Act
        Authentication authentication = proveedorTokenJWT.getAuthentication(token);

        // Assert
        assertNotNull(authentication);
        assertEquals(usuarioMock, authentication.getPrincipal());
        verify(userDetailsService).loadUserByUsername(usuarioMock.getUsername());
    }
}