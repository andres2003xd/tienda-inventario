package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.EstadoPQRS;
import com.tiendainventario.repository.EstadoPqrsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadoPqrsService extends BaseService<EstadoPQRS, Long, EstadoPqrsRepository> {

    public EstadoPqrsService(EstadoPqrsRepository repository) {
        super(repository);
    }

    @Transactional
    public EstadoPQRS crearEstadoPqrs(EstadoPQRS estadoPQRS) {
        if (repository.existsByNombre(estadoPQRS.getNombre())) {
            throw new ResourceAlreadyExistsException("EstadoPQRS", "nombre", estadoPQRS.getNombre());
        }
        return repository.save(estadoPQRS);
    }

    @Transactional
    public EstadoPQRS actualizarEstadoPqrs(Long id, EstadoPQRS estadoActualizado) {
        EstadoPQRS estadoExistente = findById(id);

        if (!estadoExistente.getNombre().equals(estadoActualizado.getNombre()) &&
                repository.existsByNombre(estadoActualizado.getNombre())) {
            throw new ResourceAlreadyExistsException("EstadoPQRS", "nombre", estadoActualizado.getNombre());
        }

        estadoExistente.setNombre(estadoActualizado.getNombre());
        estadoExistente.setDescripcion(estadoActualizado.getDescripcion());

        return repository.save(estadoExistente);
    }

    public EstadoPQRS obtenerPorNombre(String nombre) {
        return repository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoPQRS", "nombre", nombre));
    }

    @Override
    protected String getEntityName() {
        return "EstadoPQRS";
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}