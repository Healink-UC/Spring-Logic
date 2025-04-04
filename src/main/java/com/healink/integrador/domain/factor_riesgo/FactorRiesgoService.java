package com.healink.integrador.domain.factor_riesgo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import java.util.List;

@Service
@Transactional
public class FactorRiesgoService extends ServicioGenerico<FactorRiesgo> {

    private final FactorRiesgoRepository factorRepository;

    public FactorRiesgoService(FactorRiesgoRepository factorRiesgoRepository) {
        super(factorRiesgoRepository);
        this.factorRepository = factorRiesgoRepository;

    }

    @Transactional(readOnly = true)
    public List<FactorRiesgo> findByNombre(String nombreFactor) {
        return factorRepository.findByNombreContainingIgnoreCase(nombreFactor);

    }

    @Transactional(readOnly = true)
    public List<FactorRiesgo> findByTipo(String tipoFactorRiesgo) {
        return factorRepository.findByTipoParcial(tipoFactorRiesgo);

    }
}
