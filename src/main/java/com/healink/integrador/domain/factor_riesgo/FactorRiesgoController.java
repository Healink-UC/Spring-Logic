package com.healink.integrador.domain.factor_riesgo;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/factor-riesgo")
@Tag(name = "Factores de riesgo")
public class FactorRiesgoController extends ControladorGenerico<FactorRiesgo, FactorRiesgoDTO> {

    private final FactorRiesgoService factorService;

    public FactorRiesgoController(FactorRiesgoService factorService, FactorRiesgoMapper factorRiesgoMapper) {
        super(factorService, factorRiesgoMapper);
        this.factorService = factorService;
    }

    @GetMapping("/{nombreFactor}")
    public ResponseEntity<List<FactorRiesgoDTO>> getByNombre(@PathVariable String nombreFactor) {
        List<FactorRiesgo> factores = factorService.findByNombre(nombreFactor);
        return ResponseEntity.ok(mapeador.aListaDTO(factores));
    }

    @GetMapping("/{tipoFactor}")
    public ResponseEntity<List<FactorRiesgoDTO>> getbyTipo(@PathVariable String tipoFactor) {
        List<FactorRiesgo> factores = factorService.findByTipo(tipoFactor);
        return ResponseEntity.ok(mapeador.aListaDTO(factores));

    }
}
