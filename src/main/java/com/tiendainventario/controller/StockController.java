package com.tiendainventario.controller;

import com.tiendainventario.model.Stock;
import com.tiendainventario.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> crearStock(@RequestBody Stock stock) {
        Stock nuevoStock = stockService.crearStock(stock);

        // Construir la respuesta en el formato solicitado
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", nuevoStock.getId());

        Map<String, Object> producto = new LinkedHashMap<>();
        producto.put("id", nuevoStock.getProducto().getId());
        respuesta.put("producto", producto);

        respuesta.put("cantidad", nuevoStock.getCantidad());
        respuesta.put("ubicacion", nuevoStock.getUbicacion());
        respuesta.put("stockMinimo", nuevoStock.getStockMinimo());
        respuesta.put("stockMaximo", nuevoStock.getStockMaximo());

        return ResponseEntity.ok(respuesta);
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