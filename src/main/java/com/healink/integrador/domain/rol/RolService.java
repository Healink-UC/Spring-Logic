package com.healink.integrador.domain.rol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import java.util.Optional;

@Service
@Transactional
public class RolService extends ServicioGenerico<Rol> {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        super(rolRepository);
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}