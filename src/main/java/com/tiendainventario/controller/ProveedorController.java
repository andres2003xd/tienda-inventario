package com.tiendainventario.controller;

import com.tiendainventario.exception.ProveedorAlreadyExistsException;
import com.tiendainventario.exception.ProveedorNotFoundException;
import com.tiendainventario.model.Proveedor;
import com.tiendainventario.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("detalles", detalles);
        return errorResponse;
    }

    @PostMapping
    public ResponseEntity<?> crearProveedor(@RequestBody Proveedor proveedor) {
        try {
            Proveedor nuevoProveedor = proveedorService.crearProveedor(proveedor);
            return new ResponseEntity<>(nuevoProveedor, HttpStatus.CREATED);
        } catch (ProveedorAlreadyExistsException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), proveedor.getNombre()), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> listarProveedores() {
        return ResponseEntity.ok(proveedorService.listarProveedores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProveedor(@PathVariable Long id) {
        try {
            Proveedor proveedor = proveedorService.buscarPorId(id);
            return ResponseEntity.ok(proveedor);
        } catch (ProveedorNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        try {
            Proveedor proveedorActualizado = proveedorService.actualizarProveedor(id, proveedor);
            return ResponseEntity.ok(proveedorActualizado);
        } catch (ProveedorAlreadyExistsException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), proveedor.getNombre()), HttpStatus.CONFLICT);
        } catch (ProveedorNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Long id) {
        try {
            proveedorService.eliminarProveedor(id);
            return ResponseEntity.noContent().build();
        } catch (ProveedorNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }
}