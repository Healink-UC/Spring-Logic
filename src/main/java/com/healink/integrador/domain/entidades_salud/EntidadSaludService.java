package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.service.ServicioGenerico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntidadSaludService extends ServicioGenerico<EntidadSalud> {

    private final EntidadSaludRepository entidadSaludRepository;

    public EntidadSaludService(EntidadSaludRepository entidadSaludRepository) {
        super(entidadSaludRepository);
        this.entidadSaludRepository = entidadSaludRepository;
    }

    @Transactional(readOnly = true)
    public Optional<EntidadSalud> findByRazonSocial(String razon) {
        return entidadSaludRepository.findByRazonSocial(razon);
    }

    @Transactional(readOnly = true)
    public Optional<EntidadSalud> findByUsuarioId(Long usuarioId) {
        return entidadSaludRepository.findByUsuarioId(usuarioId);
    }
    
    /**
     * Buscar entidades de salud creadas por un administrador específico
     * @param tipoIdentificacion tipo de documento (ej: "CC", "NIT", "CE")
     * @param identificacion número de identificación
     * @return lista de entidades creadas por ese administrador
     */
    @Transactional(readOnly = true)
    public List<EntidadSalud> buscarPorAdministrador(String tipoIdentificacion, String identificacion) {
        // Construir el identificador en formato "TipoIdentificacion:Identificacion"
        String creadoPor = tipoIdentificacion + ":" + identificacion;
        return entidadSaludRepository.findByCreadoPor(creadoPor);
    }

}
