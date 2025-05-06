package com.healink.integrador.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.healink.integrador.domain.usuario.TipoIdentificacion;
import com.healink.integrador.domain.usuario.UsuarioDTO;

class RespuestaJWTTest {

    @Test
    void testConstructorYGetters() {
        // Arrange
        String token = "jwt-token";
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setTipoIdentificacion(TipoIdentificacion.CC);
        usuario.setIdentificacion("1234567890");
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");

        // Act
        RespuestaJWT respuesta = new RespuestaJWT(token, usuario);

        // Assert
        assertEquals(token, respuesta.getToken());
        assertEquals(usuario, respuesta.getUsuario());
    }

    @Test
    void testSettersYGetters() {
        // Arrange
        RespuestaJWT respuesta = new RespuestaJWT(null, null);
        String token = "nuevo-token";
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombres("María");

        // Act
        respuesta.setToken(token);
        respuesta.setUsuario(usuario);

        // Assert
        assertEquals(token, respuesta.getToken());
        assertEquals(usuario, respuesta.getUsuario());
    }

    @Test
    void testToString() {
        // Arrange
        String token = "jwt-token";
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombres("Juan");
        RespuestaJWT respuesta = new RespuestaJWT(token, usuario);

        // Act
        String toString = respuesta.toString();

        // Assert
        assertTrue(toString.contains("token=jwt-token"));
        assertTrue(toString.contains("usuario=" + usuario.toString()));
    }

    @Test
    void testEqualsYHashCode() {
        // Arrange
        String token = "jwt-token";
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombres("Juan");

        RespuestaJWT respuesta1 = new RespuestaJWT(token, usuario);
        RespuestaJWT respuesta2 = new RespuestaJWT(token, usuario);
        RespuestaJWT respuestaDiferente = new RespuestaJWT("otro-token", usuario);

        // Assert - equals
        assertEquals(respuesta1, respuesta2);
        assertNotEquals(respuesta1, respuestaDiferente);
        assertNotEquals(respuesta1, null);
        assertNotEquals(respuesta1, "string");

        // Assert - hashCode
        assertEquals(respuesta1.hashCode(), respuesta2.hashCode());
    }
}