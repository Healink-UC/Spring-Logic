package com.healink.integrador.domain.campana_factor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campana-factores")
@Tag(name = "Campaña factores")
public class CampanaFactoresController extends ControladorGenerico<CampanaFactores, CampanaFactoresDTO> {

    private final CampanaFactoresService campanaFactoresService;

    public CampanaFactoresController(CampanaFactoresService campanaFactoresService,
            CampanaFactoresMapper campanaFactoresMapper) {
        super(campanaFactoresService, campanaFactoresMapper);
        this.campanaFactoresService = campanaFactoresService;
    }

    @PostMapping("/campana/{campanaId}/factores")
    public ResponseEntity<List<CampanaFactoresDTO>> agregarFactoresCampana(
            @PathVariable Long campanaId,
            @RequestBody @Valid List<Long> factoresIds) {

        List<CampanaFactores> campanaFactores = campanaFactoresService.agregarFactoresCampana(campanaId, factoresIds);

        List<CampanaFactoresDTO> campanaFactoresDTO = campanaFactores.stream()
                .map(campanaFactor -> mapeador.aDTO(campanaFactor))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(campanaFactoresDTO);
    }
}
