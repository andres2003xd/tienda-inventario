package com.tiendainventario.controller;

import com.tiendainventario.exception.ProductoAlreadyExistsException;
import com.tiendainventario.exception.ProductoNotFoundException;
import com.tiendainventario.model.Producto;
import com.tiendainventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("detalles", detalles);
        return errorResponse;
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (ProductoAlreadyExistsException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), producto.getNombre()), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id) {
        try {
            Producto producto = productoService.buscarPorId(id);
            return ResponseEntity.ok(producto);
        } catch (ProductoNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (ProductoNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        } catch (ProductoAlreadyExistsException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), producto.getNombre()), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (ProductoNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }
}