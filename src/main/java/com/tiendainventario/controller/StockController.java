package com.tiendainventario.controller;

import com.tiendainventario.model.Stock;
import com.tiendainventario.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAll() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Stock> getByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(stockService.findByProductoId(productoId));
    }

    @PostMapping
    public ResponseEntity<Stock> create(@RequestBody Stock stock) {
        return new ResponseEntity<>(
                stockService.create(stock),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> update(
            @PathVariable Long id,
            @RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.update(id, stock));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}