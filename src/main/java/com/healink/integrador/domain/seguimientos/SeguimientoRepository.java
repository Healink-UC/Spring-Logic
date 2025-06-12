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

    @Query("SELECT s FROM Seguimiento s WHERE s.citacion.id = :citacionId")
    List<Seguimiento> findByCitacionId(@Param("citacionId") Long citacionId);

    @Query("SELECT s FROM Seguimiento s WHERE s.citacion.id = :citacionId")
    Page<Seguimiento> findByCitacionId(@Param("citacionId") Long citacionId, Pageable pageable);

    /**
     * NUEVO: Buscar seguimientos por paciente ID a través de la citación médica
     */
    @Query("SELECT s FROM Seguimiento s JOIN s.citacion c WHERE c.pacienteId = :pacienteId ORDER BY s.fecha_programada DESC")
    List<Seguimiento> findByPacienteId(@Param("pacienteId") Long pacienteId);

    /**
     * NUEVO: Buscar seguimientos pendientes por paciente (hoy o anteriores y no completados)
     */
    @Query("SELECT s FROM Seguimiento s JOIN s.citacion c " +
           "WHERE c.pacienteId = :pacienteId " +
           "AND s.fecha_programada <= :fechaHoy " +
           "AND (s.estado = 'PENDIENTE' OR s.estado = 'PROGRAMADO') " +
           "ORDER BY s.fecha_programada ASC")
    List<Seguimiento> findSeguimientosPendientesByPacienteId(@Param("pacienteId") Long pacienteId, @Param("fechaHoy") LocalDate fechaHoy);

    /**
     * NUEVO: Buscar seguimientos disponibles para hoy por paciente
     */
    @Query("SELECT s FROM Seguimiento s JOIN s.citacion c " +
           "WHERE c.pacienteId = :pacienteId " +
           "AND s.fecha_programada = :fechaHoy " +
           "AND (s.estado = 'PENDIENTE' OR s.estado = 'PROGRAMADO') " +
           "ORDER BY s.prioridad DESC")
    List<Seguimiento> findSeguimientosDisponiblesHoyByPacienteId(@Param("pacienteId") Long pacienteId, @Param("fechaHoy") LocalDate fechaHoy);
}
