package com.healink.integrador.domain.datos_clinicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

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
        // Ya no se calcula IMC ni se usan peso/talla
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
}