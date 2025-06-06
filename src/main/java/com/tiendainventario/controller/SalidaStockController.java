package com.tiendainventario.controller;

import com.tiendainventario.model.SalidaStock;
import com.tiendainventario.service.SalidaStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/salidas-stock")
public class SalidaStockController {

    @Autowired
    private SalidaStockService salidaStockService;

    @PostMapping
    public ResponseEntity<SalidaStock> registrarSalida(@RequestBody SalidaStock salida) {
        return ResponseEntity.ok(salidaStockService.registrarSalida(salida));
    }

    @GetMapping
    public ResponseEntity<List<SalidaStock>> listarTodas() {
        return ResponseEntity.ok(salidaStockService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalidaStock> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(salidaStockService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<SalidaStock>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(salidaStockService.obtenerPorProducto(productoId));
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<SalidaStock>> obtenerPorEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(salidaStockService.obtenerPorEmpleado(empleadoId));
    }

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<SalidaStock>> obtenerPorVenta(@PathVariable Long idVenta) {
        return ResponseEntity.ok(salidaStockService.obtenerPorVenta(idVenta));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<SalidaStock>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(salidaStockService.obtenerPorFechas(inicio, fin));
    }
}