package com.healink.integrador.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ListaNegraTokenTest {

    @Test
    void agregarYVerificarToken() {
        // Arrange
        ListaNegraToken listaNegraToken = new ListaNegraToken();
        String token = "token-de-prueba";

        // Act & Assert - inicialmente el token no está en la lista negra
        assertFalse(listaNegraToken.estaEnListaNegra(token));

        // Act - agregamos el token a la lista negra
        listaNegraToken.agregarAListaNegra(token);

        // Assert - ahora el token debería estar en la lista negra
        assertTrue(listaNegraToken.estaEnListaNegra(token));
    }

    @Test
    void verificarTokenInexistente() {
        // Arrange
        ListaNegraToken listaNegraToken = new ListaNegraToken();
        String token = "token-que-no-existe";

        // Act & Assert
        assertFalse(listaNegraToken.estaEnListaNegra(token));
    }

    @Test
    void agregarMultiplesTokens() {
        // Arrange
        ListaNegraToken listaNegraToken = new ListaNegraToken();
        String token1 = "token-1";
        String token2 = "token-2";
        String token3 = "token-3";

        // Act
        listaNegraToken.agregarAListaNegra(token1);
        listaNegraToken.agregarAListaNegra(token2);

        // Assert
        assertTrue(listaNegraToken.estaEnListaNegra(token1));
        assertTrue(listaNegraToken.estaEnListaNegra(token2));
        assertFalse(listaNegraToken.estaEnListaNegra(token3));
    }
}