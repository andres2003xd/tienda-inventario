package com.tiendainventario.controller;

import com.tiendainventario.model.Venta;
import com.tiendainventario.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService; // Ensure the "crearVenta(Map<String, Object>)" method is implemented in VentaService

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody Map<String, Object> requestBody) {
        Venta nuevaVenta = ventaService.crearVenta(requestBody); // Ensure this method exists in VentaService
        return ResponseEntity.ok(nuevaVenta);
    }

    // Listar todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        List<Venta> ventas = ventaService.listarVentas();
        return ResponseEntity.ok(ventas);
    }

    // Obtener venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.obtenerVentaPorId(id);
        if (venta.isPresent()) {
            return ResponseEntity.ok(venta.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar una venta
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {
        Venta ventaActualizada = ventaService.actualizarVenta(id, requestBody);
        return ResponseEntity.ok(ventaActualizada);
    }

    // Eliminar una venta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.ok().build();
    }
}