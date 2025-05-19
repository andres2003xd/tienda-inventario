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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    /**
     * Método para crear una venta a partir del JSON reducido
     */
    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody Map<String, Object> requestBody) {
        try {
            // 1. Extraer los datos del cuerpo de la solicitud JSON
            String fecha = (String) requestBody.get("fecha");
            Double total = Double.valueOf(requestBody.get("total").toString());
            Map<String, Object> clienteData = (Map<String, Object>) requestBody.get("cliente");
            Long clienteId = Long.valueOf(clienteData.get("id").toString());

            // 2. Obtener el cliente por su ID y crear una nueva venta
            Cliente cliente = ventaService.obtenerClientePorId(clienteId);
            Venta venta = new Venta();
            venta.setFecha(LocalDateTime.parse(fecha)); // Convertir la fecha recibida como texto
            venta.setTotal(total); // Setear el total recibido
            venta.setCliente(cliente); // Asociar la venta al cliente

            // 3. Guardar la venta usando el servicio
            Venta nuevaVenta = ventaService.crearVenta(venta);

            // 4. Construir la respuesta con el formato deseado
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("id", nuevaVenta.getId());
            respuesta.put("fecha", nuevaVenta.getFecha());
            respuesta.put("total", nuevaVenta.getTotal());
            Map<String, Object> clienteResponse = new HashMap<>();
            clienteResponse.put("id", nuevaVenta.getCliente().getId());
            respuesta.put("cliente", clienteResponse);

            // Enviar respuesta
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Método para listar todas las ventas
     */
    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    /**
     * Método para obtener una venta por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        try {
            Venta venta = ventaService.buscarPorId(id);
            return ResponseEntity.ok(venta);
        } catch (Exception e) {
            return new ResponseEntity<>(crearRespuestaError(e.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método para actualizar una venta
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            Venta ventaActualizada = ventaService.actualizarVenta(id, venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (Exception e) {
            return new ResponseEntity<>(crearRespuestaError(e.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método para eliminar una venta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        try {
            ventaService.eliminarVenta(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(crearRespuestaError(e.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Construir respuesta de error para API
     */
    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("detalles", detalles);
        return errorResponse;
    }
}