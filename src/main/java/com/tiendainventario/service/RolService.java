package com.tiendainventario.service;

import com.tiendainventario.model.Rol;
import com.tiendainventario.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tiendainventario.exception.ResourceAlreadyExistsException;

@Service
public class RolService extends BaseService<Rol, Long, RolRepository> {

    public RolService(RolRepository repository) {
        super(repository);
    }

    @Transactional
    public Rol crearRol(Rol rol) {
        String descripcion = rol.getDescripcion().toUpperCase();
        if (repository.existsByDescripcion(descripcion)) {
            throw new ResourceAlreadyExistsException("Rol", "descripcion", descripcion);
        }
        rol.setDescripcion(descripcion);
        return repository.save(rol);
    }

    @Transactional
    public Rol actualizarRol(Long id, Rol rolActualizado) {
        Rol rol = findById(id);
        rol.setDescripcion(rolActualizado.getDescripcion().toUpperCase());
        return repository.save(rol);
    }

    @Override
    protected String getEntityName() {
        return "Rol";
    }
}