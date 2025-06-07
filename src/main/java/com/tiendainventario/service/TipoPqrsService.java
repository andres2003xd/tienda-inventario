package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.TipoPQRS;
import com.tiendainventario.repository.TipoPqrsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoPqrsService extends BaseService<TipoPQRS, Long, TipoPqrsRepository> {

    public TipoPqrsService(TipoPqrsRepository repository) {
        super(repository);
    }

    @Transactional
    public TipoPQRS crearTipoPqrs(TipoPQRS tipoPQRS) {
        if (repository.existsByNombre(tipoPQRS.getNombre())) {
            throw new ResourceAlreadyExistsException("TipoPQRS", "nombre", tipoPQRS.getNombre());
        }
        return repository.save(tipoPQRS);
    }

    @Transactional
    public TipoPQRS actualizarTipoPqrs(Long id, TipoPQRS tipoActualizado) {
        TipoPQRS tipoExistente = findById(id);

        // Verificar si el nombre nuevo ya existe (y no es el mismo registro)
        if (!tipoExistente.getNombre().equals(tipoActualizado.getNombre()) &&
                repository.existsByNombre(tipoActualizado.getNombre())) {
            throw new ResourceAlreadyExistsException("TipoPQRS", "nombre", tipoActualizado.getNombre());
        }

        tipoExistente.setNombre(tipoActualizado.getNombre());
        tipoExistente.setDescripcion(tipoActualizado.getDescripcion());

        return repository.save(tipoExistente);
    }

    public TipoPQRS obtenerPorNombre(String nombre) {
        return repository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("TipoPQRS", "nombre", nombre));
    }

    @Override
    protected String getEntityName() {
        return "TipoPQRS";
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}