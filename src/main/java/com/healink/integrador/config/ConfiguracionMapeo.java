package com.healink.integrador.config;

import org.springframework.context.annotation.Configuration;

import com.healink.integrador.domain.rol.Rol;
import com.healink.integrador.domain.rol.RolRepository;

import jakarta.persistence.EntityNotFoundException;

@Configuration
public class ConfiguracionMapeo {

    private final RolRepository rolRepository;

    public ConfiguracionMapeo(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // @Bean
    public Rol rolFromId(Long id) {
        if (id == null)
            return null;
        return rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
    }
}
