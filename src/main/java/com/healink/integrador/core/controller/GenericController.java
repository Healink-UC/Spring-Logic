package com.healink.integrador.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.core.dto.BaseDTO;
import com.healink.integrador.core.entity.BaseEntity;
import com.healink.integrador.core.mapper.GenericMapper;
import com.healink.integrador.core.service.GenericService;

import java.util.List;

@RequiredArgsConstructor
public abstract class GenericController<E extends BaseEntity, D extends BaseDTO> {

    protected final GenericService<E> service;
    protected final GenericMapper<E, D> mapper;

    @GetMapping
    public ResponseEntity<List<D>> getAll() {
        List<E> entities = service.findAll();
        return ResponseEntity.ok(mapper.toDtoList(entities));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<D>> getAllPaged(Pageable pageable) {
        Page<E> page = service.findAll(pageable);
        return ResponseEntity.ok(page.map(mapper::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(entity -> ResponseEntity.ok(mapper.toDto(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<D> create(@Valid @RequestBody D dto) {
        E entity = mapper.toEntity(dto);
        return new ResponseEntity<>(mapper.toDto(service.save(entity)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable Long id, @Valid @RequestBody D dto) {
        try {
            E existingEntity = service.getById(id);
            mapper.updateEntityFromDto(dto, existingEntity);
            return ResponseEntity.ok(mapper.toDto(service.save(existingEntity)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}