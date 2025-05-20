package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class BaseService<T, ID extends Long, R extends JpaRepository<T, ID>> {
    protected final R repository;

    protected BaseService(R repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), id));
    }

    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(getEntityName(), id);
        }
        repository.deleteById(id);
    }

    protected abstract String getEntityName();
}