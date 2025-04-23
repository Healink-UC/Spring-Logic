package com.healink.integrador.domain.predicciones;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/predicciones")
@Tag(name = "Predicciones")
public class PrediccionController extends ControladorGenerico<Prediccion, PrediccionDTO> {

    public PrediccionController(PrediccionService prediccionService, PrediccionMapper prediccionMapper) {
        super(prediccionService, prediccionMapper);
    }
}
