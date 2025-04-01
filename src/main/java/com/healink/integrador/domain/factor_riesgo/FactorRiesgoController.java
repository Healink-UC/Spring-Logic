package com.healink.integrador.domain.factor_riesgo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/factor-riesgo")
public class FactorRiesgoController {

    @Autowired
    private FactorRiesgoService factorService;

    @PostMapping("/")
    public ResponseEntity<FactorRiesgo> createFactorRiesgo(@ModelAttribute FactorRiesgoDTO factorDTO) {

        FactorRiesgo factor = factorService.createFactorRiesgo(factorDTO);
        return ResponseEntity.status(201).body(factor);
    }

    @GetMapping("/")
    public ResponseEntity<List<FactorRiesgoDTO>> getFactoresRiesgo() {
        return ResponseEntity.ok(factorService.getFactoresRiesgo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactorRiesgo> getFactorRiesgoById(@PathVariable Long id) {
        Optional<FactorRiesgo> factor = factorService.getFactorRiesgoByID(id);
        return factor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FactorRiesgo> updateUser(@PathVariable Long id,
            @RequestBody FactorRiesgo newFactorRiesgo) {
        FactorRiesgo factorRiesgoUpdated = factorService.updateFactorRiesgo(id, newFactorRiesgo);
        return ResponseEntity.ok(factorRiesgoUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        factorService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
