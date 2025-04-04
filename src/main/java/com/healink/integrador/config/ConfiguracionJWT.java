package com.healink.integrador.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
@Data
public class ConfiguracionJWT {
    private String llaveSecreta;
    private long expiracion;
    private TokenRefresco tokenRefresco;

    @Data
    public static class TokenRefresco {
        private long expiracion;
    }

    // Inicialización para validar la configuración
    @PostConstruct
    public void init() {
        if (llaveSecreta == null) {
            throw new IllegalStateException(
                    "application.security.jwt.secret-key no está configurada. Revisa tus propiedades.");
        }
        System.out.println("JWT SECRET configurado correctamente: " + (llaveSecreta.length() > 0));
    }
}