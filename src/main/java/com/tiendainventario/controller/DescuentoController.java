package com.tiendainventario.controller;

import com.tiendainventario.model.Descuento;
import com.tiendainventario.service.DescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descuentos")
public class DescuentoController {

    @Autowired
    private DescuentoService descuentoService;

    @GetMapping
    public ResponseEntity<List<Descuento>> listarDescuentos() {
        return ResponseEntity.ok(descuentoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Descuento> obtenerDescuento(@PathVariable Long id) {
        return ResponseEntity.ok(descuentoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Descuento> crearDescuento(@RequestBody Descuento descuento) {
        return ResponseEntity.ok(descuentoService.crearDescuento(descuento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Descuento> actualizarDescuento(@PathVariable Long id, @RequestBody Descuento descuento) {
        return ResponseEntity.ok(descuentoService.actualizarDescuento(id, descuento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDescuento(@PathVariable Long id) {
        descuentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}