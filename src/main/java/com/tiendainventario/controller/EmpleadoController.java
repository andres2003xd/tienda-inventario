package com.tiendainventario.controller;

import com.tiendainventario.model.Empleado;
import com.tiendainventario.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarEmpleados() {

        List<Map<String, Object>> empleados = empleadoService.findAll()
                .stream()
                .map(this::convertirARespuestaJson)
                .collect(Collectors.toList());
        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerEmpleado(@PathVariable Long id) {
        // Convertir resultado a mapa antes de devolverlo
        Empleado empleado = empleadoService.findById(id);
        return ResponseEntity.ok(convertirARespuestaJson(empleado));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearEmpleado(@RequestBody Empleado empleado) {
        // Llamar al servicio para crear y convertir a JSON
        Empleado empleadoCreado = empleadoService.crearEmpleado(empleado);
        // Respuesta JSON simplificada para POST (solo incluye el id del cargo)
        return ResponseEntity.ok(convertirARespuestaJsonConCargoSimplificado(empleadoCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarEmpleado(
            @PathVariable Long id,
            @RequestBody Empleado empleado) {
        // Actualizar empleado y devolver respuesta JSON
        Empleado empleadoActualizado = empleadoService.actualizarEmpleado(id, empleado);
        return ResponseEntity.ok(convertirARespuestaJson(empleadoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> convertirARespuestaJson(Empleado empleado) {
        Map<String, Object> respuesta = new HashMap<>();
        Map<String, Object> cargoMap = new HashMap<>();

        // Incluir todos los atributos del cargo
        cargoMap.put("id", empleado.getCargo().getId());
        cargoMap.put("nombre", empleado.getCargo().getNombre());
        cargoMap.put("descripcion", empleado.getCargo().getDescripcion());
        cargoMap.put("salarioBase", empleado.getCargo().getSalarioBase());

        // Incluir atributos del empleado
        respuesta.put("cargo", cargoMap);
        respuesta.put("nombre", empleado.getNombre());
        respuesta.put("apellido", empleado.getApellido());
        respuesta.put("documento", empleado.getDocumento());
        respuesta.put("telefono", empleado.getTelefono());
        respuesta.put("email", empleado.getEmail());
        respuesta.put("fechaContratacion", empleado.getFechaContratacion());

        return respuesta;
    }

    private Map<String, Object> convertirARespuestaJsonConCargoSimplificado(Empleado empleado) {
        Map<String, Object> respuesta = new HashMap<>();
        Map<String, Object> cargoMap = new HashMap<>();

        cargoMap.put("id", empleado.getCargo().getId());


        respuesta.put("cargo", cargoMap);
        respuesta.put("nombre", empleado.getNombre());
        respuesta.put("apellido", empleado.getApellido());
        respuesta.put("documento", empleado.getDocumento());
        respuesta.put("telefono", empleado.getTelefono());
        respuesta.put("email", empleado.getEmail());
        respuesta.put("fechaContratacion", empleado.getFechaContratacion());

        return respuesta;
    }
}