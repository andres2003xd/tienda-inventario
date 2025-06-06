package com.tiendainventario.controller;

import com.tiendainventario.model.EntradaStock;
import com.tiendainventario.service.EntradaStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/entradas-stock")
public class EntradaStockController {

    @Autowired
    private EntradaStockService entradaStockService;

    @PostMapping
    public ResponseEntity<EntradaStock> registrarEntrada(@RequestBody EntradaStock entrada) {
        return ResponseEntity.ok(entradaStockService.registrarEntrada(entrada));
    }

    @GetMapping
    public ResponseEntity<List<EntradaStock>> listarTodas() {
        return ResponseEntity.ok(entradaStockService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaStock> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entradaStockService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<EntradaStock>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(entradaStockService.obtenerPorProducto(productoId));
    }

    // Nuevos endpoints agregados
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<EntradaStock>> obtenerPorProveedor(@PathVariable Long proveedorId) {
        return ResponseEntity.ok(entradaStockService.obtenerPorProveedor(proveedorId));
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<EntradaStock>> obtenerPorEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(entradaStockService.obtenerPorEmpleado(empleadoId));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<EntradaStock>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(entradaStockService.obtenerPorFechas(inicio, fin));
    }
}