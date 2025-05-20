package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.model.Proveedor;
import com.tiendainventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService extends BaseService<Proveedor, Long, ProveedorRepository> {

    public ProveedorService(ProveedorRepository repository) {
        super(repository);
    }

    public Proveedor crearProveedor(Proveedor proveedor) {
        if (repository.existsByNombre(proveedor.getNombre())) {
            throw new ResourceAlreadyExistsException("Proveedor", "nombre", proveedor.getNombre());
        }
        return repository.save(proveedor);
    }

    public Proveedor actualizarProveedor(Long id, Proveedor proveedorActualizado) {
        Proveedor proveedor = findById(id);

        if (!proveedor.getId().equals(id) && repository.existsByNombre(proveedorActualizado.getNombre())) {
            throw new ResourceAlreadyExistsException("Proveedor", "nombre", proveedorActualizado.getNombre());
        }

        proveedor.setNombre(proveedorActualizado.getNombre());
        proveedor.setTelefono(proveedorActualizado.getTelefono());
        proveedor.setEmail(proveedorActualizado.getEmail());
        return repository.save(proveedor);
    }

    @Override
    protected String getEntityName() {
        return "Proveedor";
    }
}