package com.tiendainventario.controller;

import com.tiendainventario.model.Descuento;
import com.tiendainventario.service.DescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/descuentos")
public class DescuentoController {

    @Autowired
    private DescuentoService descuentoService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarDescuentos() {

        List<Map<String, Object>> listaDescuentos = descuentoService.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(listaDescuentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDescuento(@PathVariable Long id) {
        try {

            Descuento descuento = descuentoService.findById(id);


            Map<String, Object> respuesta = convertirADto(descuento);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(buildErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crearDescuento(@RequestBody Descuento descuento) {
        try {

            Descuento nuevoDescuento = descuentoService.crearDescuento(descuento);

            Map<String, Object> respuesta = convertirADto(nuevoDescuento);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(buildErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDescuento(@PathVariable Long id, @RequestBody Descuento descuento) {
        try {

            Descuento descuentoActualizado = descuentoService.actualizarDescuento(id, descuento);


            Map<String, Object> respuesta = convertirADto(descuentoActualizado);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(buildErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDescuento(@PathVariable Long id) {
        descuentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("status", 500);
        errorResponse.put("message", "Error interno del servidor: " + message);
        errorResponse.put("timestamp", LocalDateTime.now());
        return errorResponse;
    }

    private Map<String, Object> convertirADto(Descuento descuento) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", descuento.getId());
        dto.put("nombre", descuento.getNombre());
        dto.put("porcentaje", descuento.getPorcentaje());
        dto.put("fechaInicio", descuento.getFechaInicio());
        dto.put("fechaFin", descuento.getFechaFin());
        dto.put("activo", descuento.getActivo());
        return dto;
    }
}