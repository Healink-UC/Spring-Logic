package com.healink.integrador.domain.campana_factor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/campana-factores")
@Tag(name = "Campaña factores")
public class CampanaFactoresController extends ControladorGenerico<CampanaFactores, CampanaFactoresDTO> {

    public CampanaFactoresController(CampanaFactoresService campanaFactoresService,
            CampanaFactoresMapper campanaFactoresMapper) {
        super(campanaFactoresService, campanaFactoresMapper);

    }
}
