package com.healink.integrador.domain.citaciones_medicas;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import com.healink.integrador.domain.datos_clinicos.DatosClinicos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitacionMedicaRepository extends RepositorioGenerico<CitacionMedica> {
    Optional<List<CitacionMedica>> findByCampanaId(Long campanaId);
    Optional<List<CitacionMedica>> findByPacienteId(Long pacienteId);
    Optional<List<CitacionMedica>> findByMedicoId(Long medicoId);
}