package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.Repository.RepositorioGenerico;

import java.util.List;
import java.util.Optional;

public interface ServicioCampanaRepository extends RepositorioGenerico<ServicioCampana> {
    Optional<List<ServicioCampana>> findByCampanaId(Long campanaId);

    Optional<List<ServicioCampana>> findByServicioId(Long campanaId);

    default List<ServicioCampana> saveAllServiciosCampana(Long campanaId, List<Long> serviciosIds) {
        List<ServicioCampana> serviciosCampana = serviciosIds.stream()
                .map(servicioId -> {
                    ServicioCampana servicioCampana = new ServicioCampana();
                    servicioCampana.setCampanaId(campanaId);
                    servicioCampana.setServicioId(servicioId);
                    return servicioCampana;
                })
                .toList();

        return (List<ServicioCampana>) saveAll(serviciosCampana);
    }
}
