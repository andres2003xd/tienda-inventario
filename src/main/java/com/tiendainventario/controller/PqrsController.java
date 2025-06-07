package com.tiendainventario.controller;

import com.tiendainventario.model.PQRS;
import com.tiendainventario.service.PqrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pqrs")
public class PqrsController {

    @Autowired
    private PqrsService pqrsService;

    @GetMapping
    public ResponseEntity<List<PQRS>> listarTodos() {
        return ResponseEntity.ok(pqrsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PQRS> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pqrsService.findById(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PQRS>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pqrsService.obtenerPorCliente(clienteId));
    }

    @GetMapping("/estado/{estadoId}")
    public ResponseEntity<List<PQRS>> obtenerPorEstado(@PathVariable Long estadoId) {
        return ResponseEntity.ok(pqrsService.obtenerPorEstado(estadoId));
    }

    @GetMapping("/tipo/{tipoId}")
    public ResponseEntity<List<PQRS>> obtenerPorTipo(@PathVariable Long tipoId) {
        return ResponseEntity.ok(pqrsService.obtenerPorTipo(tipoId));
    }



    @PostMapping
    public ResponseEntity<PQRS> crearPQRS(@RequestBody PQRS pqrs) {
        return ResponseEntity.ok(pqrsService.crearPQRS(pqrs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PQRS> actualizarPQRS(@PathVariable Long id, @RequestBody PQRS pqrs) {
        return ResponseEntity.ok(pqrsService.actualizarPQRS(id, pqrs));
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<PQRS> cerrarPQRS(@PathVariable Long id, @RequestBody String solucion) {
        return ResponseEntity.ok(pqrsService.cerrarPQRS(id, solucion));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPQRS(@PathVariable Long id) {
        pqrsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}