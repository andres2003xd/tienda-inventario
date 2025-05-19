package com.tiendainventario.service;

import com.tiendainventario.model.Proveedor;
import com.tiendainventario.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Crear un proveedor
    public Proveedor crearProveedor(Map<String, Object> requestBody) {
        // Lógica para crear proveedor
        return new Proveedor(); // Implementar lógica de creación
    }

    // Listar todos los proveedores
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    // Obtener proveedor por ID
    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    // Actualizar un proveedor
    public Proveedor actualizarProveedor(Long id, Map<String, Object> requestBody) {
        // Lógica para actualizar proveedor
        return new Proveedor(); // Implementar lógica de actualización
    }

    // Eliminar proveedor
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
}