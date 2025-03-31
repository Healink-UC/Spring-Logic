package com.healink.integrador;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Healink API", version = "1.0", description = "Sistema de gestión de campañas de salud"))
public class IntegradorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegradorApplication.class, args);
    }

}