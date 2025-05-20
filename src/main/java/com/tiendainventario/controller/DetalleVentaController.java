package com.tiendainventario.controller;

import com.tiendainventario.model.DetalleVenta;
import com.tiendainventario.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detallesventas")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listarDetallesVenta() {
        return ResponseEntity.ok(detalleVentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetalleVenta(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DetalleVenta> crearDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        return ResponseEntity.ok(detalleVentaService.crearDetalleVenta(detalleVenta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(@PathVariable Long id, @RequestBody DetalleVenta detalleActualizado) {
        return ResponseEntity.ok(detalleVentaService.actualizarDetalleVenta(id, detalleActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDetalleVenta(@PathVariable Long id) {
        detalleVentaService.delete(id);
        return ResponseEntity.noContent().build();
    }


}