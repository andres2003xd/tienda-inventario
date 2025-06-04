package com.tiendainventario.service;

import com.tiendainventario.model.Rol;
import com.tiendainventario.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService extends BaseService<Rol, Long, RolRepository> {

    public RolService(RolRepository repository) {
        super(repository);
    }

    @Transactional
    public Rol crearRol(Rol rol) {
        return repository.save(rol);
    }

    @Transactional
    public Rol actualizarRol(Long id, Rol rolActualizado) {
        Rol rol = findById(id);
        rol.setDescripcion(rolActualizado.getDescripcion());
        rol.setEmpleado(rolActualizado.getEmpleado());
        rol.setCliente(rolActualizado.getCliente());
        return repository.save(rol);
    }

    @Override
    protected String getEntityName() {
        return "Rol";
    }

}