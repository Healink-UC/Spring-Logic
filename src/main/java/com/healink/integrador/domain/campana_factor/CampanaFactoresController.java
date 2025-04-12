package com.healink.integrador.domain.campana_factor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

@RestController
@RequestMapping("/api/campana-factores")
public class CampanaFactoresController extends ControladorGenerico<CampanaFactores, CampanaFactoresDTO> {

    public CampanaFactoresController(CampanaFactoresService campanaFactoresService,
            CampanaFactoresMapper campanaFactoresMapper) {
        super(campanaFactoresService, campanaFactoresMapper);

    }
}
