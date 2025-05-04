package com.healink.integrador.security;

import static org.junit.jupiter.api.Assertions.*;

import com.healink.integrador.domain.usuario.TipoIdentificacion;

import org.junit.jupiter.api.Test;

class SolicitudAccesoTest {

    @Test
    void testSettersYGetters() {
        // Arrange
        SolicitudAcceso solicitud = new SolicitudAcceso();
        TipoIdentificacion tipoId = TipoIdentificacion.CC;
        String identificacion = "1234567890";
        String clave = "password";

        // Act
        solicitud.setTipoIdentificacion(tipoId);
        solicitud.setIdentificacion(identificacion);
        solicitud.setClave(clave);

        // Assert
        assertEquals(tipoId, solicitud.getTipoIdentificacion());
        assertEquals(identificacion, solicitud.getIdentificacion());
        assertEquals(clave, solicitud.getClave());
    }

    @Test
    void testToString() {
        // Arrange
        SolicitudAcceso solicitud = new SolicitudAcceso();
        solicitud.setTipoIdentificacion(TipoIdentificacion.CC);
        solicitud.setIdentificacion("1234567890");
        solicitud.setClave("password");

        // Act
        String toString = solicitud.toString();

        // Assert
        assertTrue(toString.contains("tipoIdentificacion=" + TipoIdentificacion.CC));
        assertTrue(toString.contains("identificacion=1234567890"));
        assertTrue(toString.contains("clave=password"));
    }

    @Test
    void testEqualsYHashCode() {
        // Arrange
        SolicitudAcceso solicitud1 = new SolicitudAcceso();
        solicitud1.setTipoIdentificacion(TipoIdentificacion.CC);
        solicitud1.setIdentificacion("1234567890");
        solicitud1.setClave("password");

        SolicitudAcceso solicitud2 = new SolicitudAcceso();
        solicitud2.setTipoIdentificacion(TipoIdentificacion.CC);
        solicitud2.setIdentificacion("1234567890");
        solicitud2.setClave("password");

        SolicitudAcceso solicitudDiferente = new SolicitudAcceso();
        solicitudDiferente.setTipoIdentificacion(TipoIdentificacion.NIT);
        solicitudDiferente.setIdentificacion("9876543210");
        solicitudDiferente.setClave("otherpassword");

        // Assert - equals
        assertEquals(solicitud1, solicitud2);
        assertNotEquals(solicitud1, solicitudDiferente);
        assertNotEquals(solicitud1, null);
        assertNotEquals(solicitud1, "string");

        // Assert - hashCode
        assertEquals(solicitud1.hashCode(), solicitud2.hashCode());
    }
}