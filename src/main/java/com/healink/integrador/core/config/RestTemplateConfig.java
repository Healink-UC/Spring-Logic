package com.healink.integrador.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 🔧 CONFIGURACIÓN DE RESTTEMPLATE PARA INTEGRACIONES
 * 
 * Configuración necesaria para las llamadas HTTP a servicios externos como n8n y FastAPI
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Bean de RestTemplate para uso general en el sistema
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 