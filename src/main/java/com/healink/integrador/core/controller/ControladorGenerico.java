package com.healink.integrador.core.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.core.dto.DTOBase;
import com.healink.integrador.core.entity.EntidadBase;
import com.healink.integrador.core.mapper.MapeadorGenerico;
import com.healink.integrador.core.service.ServicioGenerico;

import java.util.List;

@RequiredArgsConstructor
public abstract class ControladorGenerico<E extends EntidadBase, D extends DTOBase> {

    protected final ServicioGenerico<E> servicio;
    protected final MapeadorGenerico<E, D> mapeador;

    @GetMapping
    public ResponseEntity<List<D>> listarTodos() {
        List<E> entities = servicio.buscarTodos();
        return ResponseEntity.ok(mapeador.aListaDTO(entities));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<D>> listarTodosPaginado(Pageable paginable) {
        Page<E> page = servicio.buscarTodos(paginable);
        return ResponseEntity.ok(page.map(mapeador::aDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> buscarPorId(@PathVariable Long id) {
        return servicio.buscarPorId(id)
                .map(entity -> ResponseEntity.ok(mapeador.aDTO(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<D> crear(@Valid @RequestBody D dto) {
        dto.setId(null);
        E entity = mapeador.aEntidad(dto);
        return new ResponseEntity<>(mapeador.aDTO(servicio.guardar(entity)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<D> actualizar(@PathVariable Long id, @Valid @RequestBody D dto) {
        try {
            // Asegurarse que el ID en el DTO coincida con el de la URL
            dto.setId(id);
            E existingEntity = servicio.obtenerPorId(id);
            mapeador.actualizarEntidadDesdeDto(dto, existingEntity);
            E updatedEntity = servicio.guardar(existingEntity);
            return ResponseEntity.ok(mapeador.aDTO(updatedEntity));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            servicio.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}