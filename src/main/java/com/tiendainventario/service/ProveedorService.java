package com.tiendainventario.service;

import com.tiendainventario.exception.ProveedorAlreadyExistsException;
import com.tiendainventario.exception.ProveedorNotFoundException;
import com.tiendainventario.model.Proveedor;
import com.tiendainventario.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    public Proveedor buscarPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ProveedorNotFoundException("Proveedor no encontrado con ID: " + id));
    }

    public Proveedor crearProveedor(Proveedor proveedor) {
        if (proveedorRepository.existsByNombre(proveedor.getNombre())) {
            throw new ProveedorAlreadyExistsException(
                    "El proveedor con el nombre '" + proveedor.getNombre() + "' ya existe."
            );
        }
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizarProveedor(Long id, Proveedor proveedorActualizado) {
        Proveedor proveedor = buscarPorId(id);

        if (!proveedor.getId().equals(id) && proveedorRepository.existsByNombre(proveedorActualizado.getNombre())) {
            throw new ProveedorAlreadyExistsException(
                    "El nombre '" + proveedorActualizado.getNombre() + "' ya está en uso por otro proveedor."
            );
        }

        proveedor.setNombre(proveedorActualizado.getNombre());
        proveedor.setTelefono(proveedorActualizado.getTelefono());
        proveedor.setEmail(proveedorActualizado.getEmail());
        return proveedorRepository.save(proveedor);
    }

    public void eliminarProveedor(Long id) {
        Proveedor proveedor = buscarPorId(id);
        proveedorRepository.delete(proveedor);
    }
}