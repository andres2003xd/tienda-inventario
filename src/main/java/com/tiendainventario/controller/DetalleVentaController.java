package com.tiendainventario.controller;


import com.tiendainventario.model.DetalleVenta;
import com.tiendainventario.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detallesventas")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("detalles", detalles);
        return errorResponse;
    }

    @PostMapping
    public ResponseEntity<?> crearDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        try {
            // Crear el detalle de venta en el servicio
            DetalleVenta nuevoDetalleVenta = detalleVentaService.crearDetalleVenta(detalleVenta);

            // Devolver la respuesta reducida
            Map<String, Object> respuesta = mapearDetalleVenta(nuevoDetalleVenta);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Errores de validación del negocio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al crear el detalle de venta."));
        }
    }

    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listarDetallesVenta() {
        return ResponseEntity.ok(detalleVentaService.listarDetallesVenta());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleVenta(@PathVariable Long id) {
        try {

            DetalleVenta detalleVenta = detalleVentaService.buscarPorId(id);


            Map<String, Object> respuesta = mapearDetalleVenta(detalleVenta);
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDetalleVenta(@PathVariable Long id, @RequestBody DetalleVenta detalleActualizado) {
        try {
            DetalleVenta detalleVenta = detalleVentaService.actualizarDetalleVenta(id, detalleActualizado);
            return ResponseEntity.ok(detalleVenta);
        } catch (Exception ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDetalleVenta(@PathVariable Long id) {
        try {
            detalleVentaService.eliminarDetalleVenta(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
        }
    }

    private Map<String, Object> mapearDetalleVenta(DetalleVenta detalleVenta) {
        Map<String, Object> detalleVentaMap = new LinkedHashMap<>();

        // Información básica del DetalleVenta
        detalleVentaMap.put("id", detalleVenta.getId());
        detalleVentaMap.put("cantidad", detalleVenta.getCantidad());
        detalleVentaMap.put("precioUnitario", detalleVenta.getPrecioUnitario());
        detalleVentaMap.put("subtotal", detalleVenta.getSubtotal());

        // Solo mostrar ID del Producto
        if (detalleVenta.getProducto() != null) {
            Map<String, Object> productoMap = new LinkedHashMap<>();
            productoMap.put("id", detalleVenta.getProducto().getId());
            detalleVentaMap.put("producto", productoMap);
        }

        // Solo mostrar ID de la Venta
        if (detalleVenta.getVenta() != null) {
            Map<String, Object> ventaMap = new LinkedHashMap<>();
            ventaMap.put("id", detalleVenta.getVenta().getId());
            detalleVentaMap.put("venta", ventaMap);
        }

        return detalleVentaMap;
    }
    }


