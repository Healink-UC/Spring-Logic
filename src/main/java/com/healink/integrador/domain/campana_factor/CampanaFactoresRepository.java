package com.healink.integrador.domain.campana_factor;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;

public interface CampanaFactoresRepository extends RepositorioGenerico<CampanaFactores> {

    default List<CampanaFactores> saveAllFactoresCampana(Long campanaId, List<Long> factoresIds) {
        List<CampanaFactores> campanaFactores = factoresIds.stream()
                .map(factorId -> {
                    CampanaFactores campanaFactor = new CampanaFactores();
                    campanaFactor.setCampanaId(campanaId);
                    campanaFactor.setFactorId(factorId);
                    return campanaFactor;
                })
                .toList();

        return (List<CampanaFactores>) saveAll(campanaFactores);
    }
}
