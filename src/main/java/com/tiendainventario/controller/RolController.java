package com.tiendainventario.controller;

import com.tiendainventario.model.Rol;
import com.tiendainventario.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRol(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        return ResponseEntity.ok(rolService.crearRol(rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Long id, @RequestBody Rol rol) {
        return ResponseEntity.ok(rolService.actualizarRol(id, rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRol(@PathVariable Long id) {
        rolService.delete(id);
        return ResponseEntity.noContent().build();
    }

}