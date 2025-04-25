package com.healink.integrador.domain.campana;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/campana")
@Tag(name = "Campaña")
public class CampanaController extends ControladorGenerico<Campana, CampanaDTO> {

    private final CampanaService campanaService;

    public CampanaController(CampanaService campanaService, CampanaMapper campanaMapper) {
        super(campanaService, campanaMapper);
        this.campanaService = campanaService;

    }

    @GetMapping("/fecha-límite")
    public ResponseEntity<List<CampanaDTO>> getByFechaLimite() {
        List<Campana> campanas = this.campanaService.getByFechaLimite();
        return ResponseEntity.ok(mapeador.aListaDTO(campanas));
    }

    @GetMapping("/fecha-inicio")
    public ResponseEntity<List<CampanaDTO>> getByFechaInicio() {
        List<Campana> campanas = this.campanaService.getByFechaLimite();
        return ResponseEntity.ok(mapeador.aListaDTO(campanas));
    }

}
