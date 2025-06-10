package com.healink.integrador.domain.campana_factor;

import org.springframework.stereotype.Service;

import com.healink.integrador.core.service.ServicioGenerico;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class CampanaFactoresService extends ServicioGenerico<CampanaFactores> {

    private final CampanaFactoresRepository campanaFactoresRepository;

    public CampanaFactoresService(CampanaFactoresRepository campanaFactoresRepository) {
        super(campanaFactoresRepository);
        this.campanaFactoresRepository = campanaFactoresRepository;
    }

    @Transactional
    public List<CampanaFactores> agregarFactoresCampana(Long campanaId, List<Long> factoresIds) {
        return this.campanaFactoresRepository.saveAllFactoresCampana(campanaId, factoresIds);
    }

}
