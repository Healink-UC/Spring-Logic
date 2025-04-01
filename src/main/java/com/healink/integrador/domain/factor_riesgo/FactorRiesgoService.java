package com.healink.integrador.domain.factor_riesgo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

@Service
public class FactorRiesgoService {

    @Autowired
    private FactorRiesgoRepository factorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<FactorRiesgoDTO> getFactoresRiesgo() {
        List<FactorRiesgo> factores = factorRepository.findAll();
        return factores.stream().map(factor -> modelMapper.map(factor, FactorRiesgoDTO.class))
                .collect(Collectors.toList());
    }

    public FactorRiesgo createFactorRiesgo(FactorRiesgoDTO factorDTO) {
        FactorRiesgo factor = new FactorRiesgo();
        factor.setNombre(factorDTO.getNombre());
        factor.setDescripcion(factorDTO.getDescripcion());
        factor.setTipo(factorDTO.getTipo());
        System.out.println(factor);
        return factorRepository.save(factor);
    }

    public Optional<FactorRiesgo> getFactorRiesgoByID(Long id) {
        return factorRepository.findById(id);
    }

    public FactorRiesgo updateFactorRiesgo(Long factorRiesgoID, FactorRiesgo newFactorRiesgo) {
        FactorRiesgo factor = factorRepository.findById(factorRiesgoID)
                .orElseThrow(() -> new RuntimeException("Factor riesgo no encontrado!"));

        factor.setNombre(newFactorRiesgo.getNombre());
        factor.setDescripcion(newFactorRiesgo.getDescripcion());
        factor.setTipo(newFactorRiesgo.getTipo());

        return factorRepository.save(factor);
    }

    public void deleteUser(Long factorRiesgoID) {
        FactorRiesgo factor = factorRepository.findById(factorRiesgoID)
                .orElseThrow(() -> new RuntimeException("Factor riesgo no encontrado!"));

        factorRepository.delete(factor);
    }
}
