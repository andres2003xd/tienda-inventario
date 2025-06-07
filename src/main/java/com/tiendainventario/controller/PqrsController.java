package com.tiendainventario.controller;

import com.tiendainventario.model.PQRS;
import com.tiendainventario.service.PqrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        PQRS pqrs = pqrsService.findById(id);

        // Construir respuesta personalizada
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", pqrs.getId());

        // Cliente
        if (pqrs.getCliente() != null) {
            Map<String, Object> cliente = new LinkedHashMap<>();
            cliente.put("id", pqrs.getCliente().getId());
            cliente.put("nombre", pqrs.getCliente().getNombre());
            cliente.put("email", pqrs.getCliente().getEmail());
            cliente.put("direccion", pqrs.getCliente().getDireccion());
            cliente.put("telefono", pqrs.getCliente().getTelefono());
            respuesta.put("cliente", cliente);
        }

        // Tipo de PQRS
        if (pqrs.getTipo() != null) {
            Map<String, Object> tipo = new LinkedHashMap<>();
            tipo.put("id", pqrs.getTipo().getId());
            tipo.put("nombre", pqrs.getTipo().getNombre());
            tipo.put("descripcion", pqrs.getTipo().getDescripcion());
            respuesta.put("tipo", tipo);
        }

        // Estado de PQRS
        if (pqrs.getEstado() != null) {
            Map<String, Object> estado = new LinkedHashMap<>();
            estado.put("id", pqrs.getEstado().getId());
            estado.put("nombre", pqrs.getEstado().getNombre());
            estado.put("descripcion", pqrs.getEstado().getDescripcion());
            respuesta.put("estado", estado);
        }

        // Detalles de PQRS
        respuesta.put("titulo", pqrs.getTitulo());
        respuesta.put("descripcion", pqrs.getDescripcion());
        respuesta.put("fechaCreacion", pqrs.getFechaCreacion());

        return ResponseEntity.ok(respuesta);
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