package com.tiendainventario.controller;

import com.tiendainventario.model.HistorialLogin;
import com.tiendainventario.service.HistorialLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-login")
public class HistorialLoginController {

    @Autowired
    private HistorialLoginService historialLoginService;

    @GetMapping
    public ResponseEntity<List<HistorialLogin>> listarHistoriales() {
        return ResponseEntity.ok(historialLoginService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialLogin> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(historialLoginService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HistorialLogin> crearHistorial(@RequestBody HistorialLogin historialLogin) {
        return ResponseEntity.ok(historialLoginService.crearHistorial(historialLogin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialLogin> actualizarHistorial(@PathVariable Long id, @RequestBody HistorialLogin historialLogin) {
        return ResponseEntity.ok(historialLoginService.actualizarHistorial(id, historialLogin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHistorial(@PathVariable Long id) {
        historialLoginService.delete(id);
        return ResponseEntity.noContent().build();
    }
}