package com.healink.integrador.domain.campana_factor;

import org.springframework.stereotype.Service;

import com.healink.integrador.core.service.ServicioGenerico;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CampanaFactoresService extends ServicioGenerico<CampanaFactores> {

    public CampanaFactoresService(CampanaFactoresRepository campanaFactoresRepository) {
        super(campanaFactoresRepository);
    }

}
