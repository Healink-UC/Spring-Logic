package com.healink.integrador.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.Repository.RepositorioGenerico;
import com.healink.integrador.core.entity.EntidadBase;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
public abstract class ServicioGenerico<T extends EntidadBase> {

    protected final RepositorioGenerico<T> repositorio;

    @Transactional(readOnly = true)
    public List<T> buscarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Page<T> buscarTodos(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<T> buscarTodos(Specification<T> spec, Pageable pageable) {
        return repositorio.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<T> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    @Transactional(readOnly = true)
    public T obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entidad con ID " + id + " no encontrada"));
    }

    public T guardar(T entity) {
        return repositorio.save(entity);
    }

    public List<T> guardarTodos(List<T> entities) {
        return repositorio.saveAll(entities);
    }

    public void eliminarPorId(Long id) {
        if (!repositorio.existsById(id)) {
            throw new EntityNotFoundException("Entidad con ID " + id + " no encontrada");
        }
        repositorio.deleteById(id);
    }

    public void delete(T entity) {
        repositorio.delete(entity);
    }
}