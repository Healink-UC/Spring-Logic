package com.healink.integrador.domain.datos_clinicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DatosClinicosService extends ServicioGenerico<DatosClinicos> {

    private final DatosClinicosRepository datosClinicosRepository;

    public DatosClinicosService(DatosClinicosRepository datosClinicosRepository) {
        super(datosClinicosRepository);
        this.datosClinicosRepository = datosClinicosRepository;
    }

    @Override
    public DatosClinicos guardar(DatosClinicos datosClinicos) {
        // Calcular IMC si peso y talla están disponibles
        if (datosClinicos.getPeso() != null && datosClinicos.getTalla() != null && datosClinicos.getTalla() > 0) {
            double centimetrosEnMetros = 100;
            double tallaMts = datosClinicos.getTalla() / centimetrosEnMetros;
            datosClinicos.setImc(datosClinicos.getPeso() / (tallaMts * tallaMts));
        }

        return super.guardar(datosClinicos);
    }

    @Transactional(readOnly = true)
    public List<DatosClinicos> buscarPorPacienteId(Long pacienteId) {
        return datosClinicosRepository.findByPacienteId(pacienteId);
    }

    @Transactional(readOnly = true)
    public Page<DatosClinicos> buscarPorPacienteId(Long pacienteId, Pageable pageable) {
        return datosClinicosRepository.findByPacienteId(pacienteId, pageable);
    }

    @Transactional(readOnly = true)
    public List<DatosClinicos> buscarPorPacienteYRangoFechas(
            Long pacienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        return datosClinicosRepository.findByPacienteIdAndFechaRegistroBetween(pacienteId, fechaInicio, fechaFin);
    }
}