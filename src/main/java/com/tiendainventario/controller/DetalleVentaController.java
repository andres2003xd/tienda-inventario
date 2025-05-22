package com.tiendainventario.controller;

import com.tiendainventario.model.DetalleVenta;
import com.tiendainventario.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detallesventas")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarDetallesVenta() {
        List<DetalleVenta> detallesVenta = detalleVentaService.findAll();
        List<Map<String, Object>> respuesta = detallesVenta.stream()
                .map(this::transformarDetalleVentaVista)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerDetalleVenta(@PathVariable Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);
        return ResponseEntity.ok(transformarDetalleVentaVista(detalleVenta));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        DetalleVenta detalle = detalleVentaService.crearDetalleVenta(detalleVenta);
        return ResponseEntity.ok(transformarDetalleVentaBasico(detalle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarDetalleVenta(@PathVariable Long id, @RequestBody DetalleVenta detalleActualizado) {
        DetalleVenta detalleVenta = detalleVentaService.actualizarDetalleVenta(id, detalleActualizado);
        return ResponseEntity.ok(transformarDetalleVentaBasico(detalleVenta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDetalleVenta(@PathVariable Long id) {
        detalleVentaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> transformarDetalleVentaBasico(DetalleVenta detalleVenta) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", detalleVenta.getId());
        response.put("cantidad", detalleVenta.getCantidad());
        response.put("precioUnitario", detalleVenta.getPrecioUnitario());
        response.put("subtotal", detalleVenta.getSubtotal());

        Map<String, Object> producto = new LinkedHashMap<>();
        producto.put("id", detalleVenta.getProducto().getId());
        response.put("producto", producto);

        Map<String, Object> venta = new LinkedHashMap<>();
        venta.put("id", detalleVenta.getVenta().getId());
        response.put("venta", venta);

        return response;
    }

    private Map<String, Object> transformarDetalleVentaVista(DetalleVenta detalleVenta) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", detalleVenta.getId());
        response.put("cantidad", detalleVenta.getCantidad());
        response.put("precioUnitario", detalleVenta.getPrecioUnitario());
        response.put("subtotal", detalleVenta.getSubtotal());
        response.put("producto", detalleVenta.getProducto());
        response.put("venta", detalleVenta.getVenta());
        return response;
    }
}