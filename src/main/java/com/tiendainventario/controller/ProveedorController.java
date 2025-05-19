package com.tiendainventario.controller;

import com.tiendainventario.model.Proveedor;
import com.tiendainventario.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Crear proveedor
    @PostMapping
    public ResponseEntity<?> crearProveedor(@RequestBody Map<String, Object> requestBody) {
        Proveedor nuevoProveedor = proveedorService.crearProveedor(requestBody);
        return ResponseEntity.ok(nuevoProveedor);
    }

    // Listar proveedores
    @GetMapping
    public ResponseEntity<List<Proveedor>> listarProveedores() {
        List<Proveedor> proveedores = proveedorService.listarProveedores();
        return ResponseEntity.ok(proveedores);
    }

    // Obtener proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProveedor(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorService.obtenerProveedorPorId(id);
        if (proveedor.isPresent()) {
            return ResponseEntity.ok(proveedor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar proveedor
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {
        Proveedor proveedorActualizado = proveedorService.actualizarProveedor(id, (Map<String, Object>)requestBody);
        return ResponseEntity.ok(proveedorActualizado);
    }

    // Eliminar proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.ok().build();
    }
}