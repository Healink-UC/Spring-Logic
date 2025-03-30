package com.healink.integrador.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
}