package com.tiendainventario.controller;

import com.tiendainventario.exception.DetalleVentaAlreadyExistsException;
import com.tiendainventario.model.DetalleVenta;
import com.tiendainventario.model.Producto;
import com.tiendainventario.model.Venta;
import com.tiendainventario.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
            DetalleVenta nuevoDetalle = detalleVentaService.crearDetalleVenta(detalleVenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
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
            return ResponseEntity.ok(mapearDetalleVenta(detalleVenta));
        } catch (Exception ex) {
            return new ResponseEntity<>(crearRespuestaError(ex.getMessage(), "ID: " + id), HttpStatus.NOT_FOUND);
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
    // Mapeo manual para controlar la salida JSON
    private Map<String, Object> mapearDetalleVenta(DetalleVenta detalleVenta) {
        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("id", detalleVenta.getId());
        respuesta.put("cantidad", detalleVenta.getCantidad());
        respuesta.put("precioUnitario", detalleVenta.getPrecioUnitario());
        respuesta.put("subtotal", detalleVenta.getSubtotal());

        // Mapeo del producto
        Producto producto = detalleVenta.getProducto();
        if (producto != null) {
            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("id", producto.getId());
            productoMap.put("nombre", producto.getNombre());
            productoMap.put("descripcion", producto.getDescripcion());

            // Verificar si el proveedor no es null
            if (producto.getProveedor() != null) {
                productoMap.put("proveedor", producto.getProveedor() != null ? producto.getProveedor().getId() : null);
            }
            respuesta.put("producto", productoMap);
        }

        // Mapeo de la venta
        Venta venta = detalleVenta.getVenta();
        if (venta != null) {
            Map<String, Object> ventaMap = new HashMap<>();
            ventaMap.put("id", venta.getId());
            ventaMap.put("fecha", venta.getFecha());
            ventaMap.put("total", venta.getTotal());
            respuesta.put("venta", ventaMap);
        }

        return respuesta;
    }
    }


