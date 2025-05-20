package com.tiendainventario.controller;

import com.tiendainventario.model.Cliente;
import com.tiendainventario.model.Venta;
import com.tiendainventario.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody Map<String, Object> requestBody) {
        try {
            // Validar y extraer fecha
            Object fechaObject = requestBody.get("fecha");
            if (fechaObject == null || !(fechaObject instanceof String)) {
                throw new IllegalArgumentException("La fecha es obligatoria y debe estar en formato de texto.");
            }
            String fecha = (String) fechaObject;

            // Validar y extraer total
            Object totalObject = requestBody.get("total");
            if (totalObject == null) {
                throw new IllegalArgumentException("El total es obligatorio.");
            }
            Double total;
            try {
                total = Double.valueOf(totalObject.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El valor 'total' debe ser numérico.");
            }

            // Validar "cliente" como un mapa
            Object clienteObject = requestBody.get("cliente");
            if (!(clienteObject instanceof Map)) {
                throw new IllegalArgumentException("El cliente debe estar en formato JSON válido.");
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> clienteData = (Map<String, Object>) clienteObject;

            // Validar que "ID" del cliente exista y sea válido
            Object idObject = clienteData.get("id");
            if (idObject == null) {
                throw new IllegalArgumentException("El cliente debe contener un ID válido.");
            }
            Long clienteId;
            try {
                clienteId = Long.valueOf(idObject.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El ID del cliente debe ser un número.");
            }

            // Buscar cliente
            Cliente cliente = ventaService.obtenerClientePorId(clienteId);
            if (cliente == null) {
                throw new IllegalArgumentException("No se encontró un cliente con el ID proporcionado.");
            }

            // Crear y configurar nueva venta
            Venta venta = new Venta();
            venta.setFecha(LocalDateTime.parse(fecha));
            venta.setTotal(total);
            venta.setCliente(cliente);

            // Guardar la venta usando el servicio
            Venta nuevaVenta = ventaService.crearVenta(venta);

            // Construir la respuesta ordenada y en formato JSON
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("id", nuevaVenta.getId());
            respuesta.put("fecha", nuevaVenta.getFecha());
            respuesta.put("total", nuevaVenta.getTotal());
            Map<String, Object> clienteResponse = new LinkedHashMap<>();
            clienteResponse.put("id", nuevaVenta.getCliente().getId());
            respuesta.put("cliente", clienteResponse);

            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al procesar la solicitud."));
        }
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        try {
            Venta venta = ventaService.buscarPorId(id);
            return ResponseEntity.ok(venta);
        } catch (Exception e) {
            return new ResponseEntity<>(crearRespuestaError(e.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            Venta ventaActualizada = ventaService.actualizarVenta(id, venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (Exception e) {
            return new ResponseEntity<>(crearRespuestaError(e.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        try {
            ventaService.eliminarVenta(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(crearRespuestaError(e.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("detalles", detalles);
        return errorResponse;
    }
}