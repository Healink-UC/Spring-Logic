package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitacionMedicaRepository extends RepositorioGenerico<CitacionMedica> {
    Optional<List<CitacionMedica>> findByCampanaId(Long campanaId);

    Optional<List<CitacionMedica>> findByPacienteId(Long pacienteId);

    Optional<List<CitacionMedica>> findByMedicoId(Long medicoId);
}