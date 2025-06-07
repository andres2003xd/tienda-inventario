package com.tiendainventario.controller;

import com.tiendainventario.model.TipoPQRS;
import com.tiendainventario.service.TipoPqrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-pqrs")
public class TipoPqrsController {

    @Autowired
    private TipoPqrsService tipoPqrsService;

    @GetMapping
    public ResponseEntity<List<TipoPQRS>> listarTodos() {
        return ResponseEntity.ok(tipoPqrsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoPQRS> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoPqrsService.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoPQRS> obtenerPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(tipoPqrsService.obtenerPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<TipoPQRS> crearTipoPqrs(@RequestBody TipoPQRS tipoPQRS) {
        return ResponseEntity.ok(tipoPqrsService.crearTipoPqrs(tipoPQRS));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoPQRS> actualizarTipoPqrs(@PathVariable Long id, @RequestBody TipoPQRS tipoPQRS) {
        return ResponseEntity.ok(tipoPqrsService.actualizarTipoPqrs(id, tipoPQRS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTipoPqrs(@PathVariable Long id) {
        tipoPqrsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}