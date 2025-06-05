package com.tiendainventario.controller;

import com.tiendainventario.model.CargoEmpleado;
import com.tiendainventario.service.CargoEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargos-empleado")
public class CargoEmpleadoController {

    @Autowired
    private CargoEmpleadoService cargoService;

    @GetMapping
    public ResponseEntity<List<CargoEmpleado>> listarCargos() {
        return ResponseEntity.ok(cargoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoEmpleado> obtenerCargo(@PathVariable Long id) {
        return ResponseEntity.ok(cargoService.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CargoEmpleado> obtenerCargoPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(cargoService.obtenerCargoPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<CargoEmpleado> crearCargo(@RequestBody CargoEmpleado cargo) {
        return ResponseEntity.ok(cargoService.crearCargo(cargo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargoEmpleado> actualizarCargo(@PathVariable Long id, @RequestBody CargoEmpleado cargo) {
        return ResponseEntity.ok(cargoService.actualizarCargo(id, cargo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCargo(@PathVariable Long id) {
        cargoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}