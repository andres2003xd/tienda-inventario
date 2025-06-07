package com.tiendainventario.controller;

import com.tiendainventario.model.EstadoPQRS;
import com.tiendainventario.service.EstadoPqrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-pqrs")
public class EstadoPqrsController {

    @Autowired
    private EstadoPqrsService estadoPqrsService;

    @GetMapping
    public ResponseEntity<List<EstadoPQRS>> listarTodos() {
        return ResponseEntity.ok(estadoPqrsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoPQRS> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estadoPqrsService.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EstadoPQRS> obtenerPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(estadoPqrsService.obtenerPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<EstadoPQRS> crearEstadoPqrs(@RequestBody EstadoPQRS estadoPQRS) {
        return ResponseEntity.ok(estadoPqrsService.crearEstadoPqrs(estadoPQRS));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoPQRS> actualizarEstadoPqrs(@PathVariable Long id, @RequestBody EstadoPQRS estadoPQRS) {
        return ResponseEntity.ok(estadoPqrsService.actualizarEstadoPqrs(id, estadoPQRS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstadoPqrs(@PathVariable Long id) {
        estadoPqrsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}