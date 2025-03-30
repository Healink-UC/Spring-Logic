package com.healink.integrador.core.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        Page<E> pagina = servicio.buscarTodos(paginable);
        return ResponseEntity.ok(pagina.map(mapeador::aDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> buscarPorId(@PathVariable Long id) {
        return servicio.buscarPorId(id)
                .map(entidad -> ResponseEntity.ok(mapeador.aDTO(entidad)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<D> crear(@Valid @RequestBody D dto) {
        log.info("Creando nueva entidad");
        // Asegurar que el ID sea nulo para evitar actualizaciones accidentales
        dto.setId(null);
        try {
            E entity = mapeador.aEntidad(dto);
            E saved = servicio.guardar(entity);
            log.info("Entidad creada con ID: {}", saved.getId());
            return new ResponseEntity<>(mapeador.aDTO(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear entidad", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<D> actualizar(@PathVariable Long id, @Valid @RequestBody D dto) {
        log.info("Actualizando entidad con ID: {}", id);
        try {
            // Establecer explícitamente el ID del DTO
            dto.setId(id);

            // Obtener la entidad existente
            E existingEntity = servicio.obtenerPorId(id);
            log.debug("Entidad existente recuperada: {}", existingEntity);

            // Actualizar entidad con DTO
            mapeador.actualizarEntidadDesdeDTO(dto, existingEntity);
            log.debug("Entidad actualizada con datos del DTO");

            // Guardar la entidad actualizada
            E updatedEntity = servicio.guardar(existingEntity);
            log.info("Entidad actualizada exitosamente con ID: {}", updatedEntity.getId());

            return ResponseEntity.ok(mapeador.aDTO(updatedEntity));
        } catch (EntityNotFoundException e) {
            log.warn("Entidad no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar entidad con ID: {}", id, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando entidad con ID: {}", id);
        try {
            servicio.eliminarPorId(id);
            log.info("Entidad eliminada con ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.warn("Entidad no encontrada para eliminar con ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al eliminar entidad con ID: {}", id, e);
            throw e;
        }
    }
}