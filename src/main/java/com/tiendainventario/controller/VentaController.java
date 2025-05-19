package com.tiendainventario.controller;

import com.tiendainventario.exception.VentaNotFoundException;
import com.tiendainventario.model.Venta;
import com.tiendainventario.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("detalles", detalles);
        return errorResponse;
    }

    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody Venta venta) {
        Venta nuevaVenta = ventaService.crearVenta(venta);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        try {
            Venta venta = ventaService.buscarPorId(id);
            return ResponseEntity.ok(venta);
        } catch (VentaNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            Venta ventaActualizada = ventaService.actualizarVenta(id, venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (VentaNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        try {
            ventaService.eliminarVenta(id);
            return ResponseEntity.noContent().build();
        } catch (VentaNotFoundException ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }
}