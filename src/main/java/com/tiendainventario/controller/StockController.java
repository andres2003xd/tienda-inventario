package com.tiendainventario.controller;

import com.tiendainventario.model.Stock;
import com.tiendainventario.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> listarStock() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> obtenerStock(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Stock> obtenerStockPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(stockService.obtenerStockPorProducto(productoId));
    }

    @PostMapping
    public ResponseEntity<Stock> crearStock(@RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.crearStock(stock));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> actualizarStock(@PathVariable Long id, @RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.actualizarStock(id, stock));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarStock(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}