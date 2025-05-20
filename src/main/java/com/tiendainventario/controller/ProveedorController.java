package com.tiendainventario.controller;

import com.tiendainventario.model.Proveedor;
import com.tiendainventario.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> listarProveedores() {
        return ResponseEntity.ok(proveedorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerProveedor(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Proveedor> crearProveedor(@RequestBody Proveedor proveedor) {
        return ResponseEntity.ok(proveedorService.crearProveedor(proveedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        return ResponseEntity.ok(proveedorService.actualizarProveedor(id, proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Long id) {
        proveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}