package com.healink.integrador.domain.seguimientos;

import java.util.List;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

@Repository
public interface SeguimientoRepository extends RepositorioGenerico<Seguimiento> {

    List<Seguimiento> findByAtencionId(Long atencion_id);

    Page<Seguimiento> findByAtencionId(Long atencion_id, Pageable pageable);

    /**
     * NUEVO: Buscar seguimientos por paciente ID a través de la atención médica
     */
    @Query("SELECT s FROM Seguimiento s JOIN s.atencion a JOIN a.citacionMedica c WHERE c.pacienteId = :pacienteId ORDER BY s.fecha_programada DESC")
    List<Seguimiento> findByPacienteId(@Param("pacienteId") Long pacienteId);

    /**
     * NUEVO: Buscar seguimientos pendientes por paciente (hoy o anteriores y no completados)
     */
    @Query("SELECT s FROM Seguimiento s JOIN s.atencion a JOIN a.citacionMedica c " +
           "WHERE c.pacienteId = :pacienteId " +
           "AND s.fecha_programada <= :fechaHoy " +
           "AND (s.estado = 'PENDIENTE' OR s.estado = 'PROGRAMADO') " +
           "ORDER BY s.fecha_programada ASC")
    List<Seguimiento> findSeguimientosPendientesByPacienteId(@Param("pacienteId") Long pacienteId, @Param("fechaHoy") LocalDate fechaHoy);

    /**
     * NUEVO: Buscar seguimientos disponibles para hoy por paciente
     */
    @Query("SELECT s FROM Seguimiento s JOIN s.atencion a JOIN a.citacionMedica c " +
           "WHERE c.pacienteId = :pacienteId " +
           "AND s.fecha_programada = :fechaHoy " +
           "AND (s.estado = 'PENDIENTE' OR s.estado = 'PROGRAMADO') " +
           "ORDER BY s.prioridad DESC")
    List<Seguimiento> findSeguimientosDisponiblesHoyByPacienteId(@Param("pacienteId") Long pacienteId, @Param("fechaHoy") LocalDate fechaHoy);
}
