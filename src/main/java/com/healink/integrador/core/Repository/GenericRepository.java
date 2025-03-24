package com.healink.integrador.core.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.healink.integrador.core.entity.BaseEntity;

@NoRepositoryBean
public interface GenericRepository<T extends BaseEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    // Métodos adicionales comunes a todos los repositorios
}
