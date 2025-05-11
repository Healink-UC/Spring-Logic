package com.healink.integrador.domain.triaje;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/triaje")
@Tag(name = "Triaje")
public class TriajeController extends ControladorGenerico<Triaje, TriajeDTO> {

    public TriajeController(TriajeService triajeService, TriajeMapper triajeMapper) {
        super(triajeService, triajeMapper);
    }

}