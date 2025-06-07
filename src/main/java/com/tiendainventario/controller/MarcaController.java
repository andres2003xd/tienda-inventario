package com.tiendainventario.controller;

import com.tiendainventario.model.Marca;
import com.tiendainventario.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarMarcas() {

        List<Map<String, Object>> marcas = marcaService.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMarca(@PathVariable Long id) {
        try {

            Marca marca = marcaService.findById(id);

            Map<String, Object> respuesta = convertirADto(marca);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(buildErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crearMarca(@RequestBody Marca marca) {
        try {

            Marca nuevaMarca = marcaService.crearMarca(marca);

            Map<String, Object> respuesta = convertirADto(nuevaMarca);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(buildErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMarca(@PathVariable Long id, @RequestBody Marca marca) {
        try {

            Marca marcaActualizada = marcaService.actualizarMarca(id, marca);

            Map<String, Object> respuesta = convertirADto(marcaActualizada);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(buildErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMarca(@PathVariable Long id) {
        marcaService.delete(id);
        return ResponseEntity.noContent().build();
    }


    private Map<String, Object> convertirADto(Marca marca) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", marca.getId());
        dto.put("nombre", marca.getNombre());
        dto.put("descripcion", marca.getDescripcion());
        dto.put("fechaCreacion", marca.getFechaCreacion());
        return dto;
    }

    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 500);
        errorResponse.put("message", "Error interno del servidor: " + message);
        return errorResponse;
    }
}