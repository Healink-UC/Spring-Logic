package com.healink.integrador.domain.campana;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

@Service
@Transactional
public class CampanaService extends ServicioGenerico<Campana> {

    public final CampanaRepository campanaRepository;

    public CampanaService(CampanaRepository campanaRespository) {
        super(campanaRespository);
        this.campanaRepository = campanaRespository;
    }

    @Transactional(readOnly = true)
    public List<Campana> getByFechaInicio() {
        return this.campanaRepository.findAllByOrderByFechaInicioAsc();

    }

    @Transactional(readOnly = true)
    public List<Campana> getByFechaLimite() {
        return this.campanaRepository.findAllByOrderByFechaLimiteInscripcionAsc();

    }

}
