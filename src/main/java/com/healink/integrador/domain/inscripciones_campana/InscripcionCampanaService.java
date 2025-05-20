package com.healink.integrador.domain.inscripciones_campana;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.healink.integrador.core.service.ServicioGenerico;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InscripcionCampanaService extends ServicioGenerico<InscripcionCampana> {

    private final InscripcionCampanaRepository inscripcionCampanaRepository;

    public InscripcionCampanaService(InscripcionCampanaRepository inscripcionCampanaRepository) {
        super(inscripcionCampanaRepository);
        this.inscripcionCampanaRepository = inscripcionCampanaRepository;
    }

    @Transactional(readOnly = true)
    public List<InscripcionCampana> buscarPorPacienteId(Long pacienteId) {
        return inscripcionCampanaRepository.findByPacienteId(pacienteId);
    }

    @Transactional(readOnly = true)
    public List<InscripcionCampana> buscarPorCampanaId(Long campanaId) {
        return inscripcionCampanaRepository.findByCampanaId(campanaId);
    }

    @Transactional(readOnly = true)
    public List<InscripcionCampana> buscarInscripcionesActivas(Long pacienteId) {
        return inscripcionCampanaRepository.findByPacienteIdAndEstado(pacienteId, EstadoInscripcion.INSCRITO);
    }

    @Transactional
    public InscripcionCampana crearInscripcion(InscripcionCampana inscripcion) {
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado(EstadoInscripcion.INSCRITO);
        return guardar(inscripcion);
    }

    @Transactional
    public InscripcionCampana retirarInscripcion(Long id, String motivo) {
        InscripcionCampana inscripcion = buscarPorId(id).orElseThrow();
        inscripcion.setEstado(EstadoInscripcion.RETIRADO);
        inscripcion.setMotivoRetiro(motivo);
        return guardar(inscripcion);
    }
}