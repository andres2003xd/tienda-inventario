package com.tiendainventario.controller;

import com.tiendainventario.model.HistorialLogin;
import com.tiendainventario.service.HistorialLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historial-login")
public class HistorialLoginController {

    @Autowired
    private HistorialLoginService historialLoginService;

    @GetMapping
    public ResponseEntity<List<HistorialLogin>> listarHistoriales() {
        List<HistorialLogin> historiales = historialLoginService.findAll();
        return ResponseEntity.ok(historiales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerHistorial(@PathVariable Long id) {
        HistorialLogin historial = historialLoginService.findById(id);

        // Convertimos el historial al formato necesario
        Map<String, Object> response = new HashMap<>();
        response.put("fechaLogin", historial.getFechaLogin());
        response.put("ip", historial.getIp());
        response.put("dispositivo", historial.getDispositivo());
        response.put("usuario", historial.getUsuarioAsJson());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearHistorial(@RequestBody HistorialLogin historialLogin) {
        HistorialLogin nuevoHistorial = historialLoginService.crearHistorial(historialLogin);


        Map<String, Object> response = new HashMap<>();
        response.put("fechaLogin", nuevoHistorial.getFechaLogin());
        response.put("ip", nuevoHistorial.getIp());
        response.put("dispositivo", nuevoHistorial.getDispositivo());
        response.put("usuario", nuevoHistorial.getUsuarioAsJson());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarHistorial(
            @PathVariable Long id,
            @RequestBody HistorialLogin historialLogin
    ) {
        HistorialLogin historialActualizado = historialLoginService.actualizarHistorial(id, historialLogin);


        Map<String, Object> response = new HashMap<>();
        response.put("fechaLogin", historialActualizado.getFechaLogin());
        response.put("ip", historialActualizado.getIp());
        response.put("dispositivo", historialActualizado.getDispositivo());
        response.put("usuario", historialActualizado.getUsuarioAsJson());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHistorial(@PathVariable Long id) {
        historialLoginService.eliminarById(id);
        return ResponseEntity.noContent().build();
    }
}