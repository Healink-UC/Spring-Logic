package com.healink.integrador.domain.servicios_campanas;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import com.healink.integrador.domain.entidades_salud.EntidadSalud;
// import com.healink.integrador.domain.servicios_medicos.ServicioMedico;

import java.util.List;
import java.util.Optional;

public interface ServicioCampanaRepository extends RepositorioGenerico<ServicioCampana> {
    Optional<List<ServicioCampana>> findByCampanaId(Long campanaId);
    Optional<List<ServicioCampana>> findByServicioId(Long campanaId);
}
