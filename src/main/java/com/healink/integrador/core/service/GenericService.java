package com.healink.integrador.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.Repository.GenericRepository;
import com.healink.integrador.core.entity.BaseEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
public abstract class GenericService<T extends BaseEntity> {

    protected final GenericRepository<T> repository;

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public T getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entidad con ID " + id + " no encontrada"));
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public List<T> saveAll(List<T> entities) {
        return repository.saveAll(entities);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Entidad con ID " + id + " no encontrada");
        }
        repository.deleteById(id);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }
}