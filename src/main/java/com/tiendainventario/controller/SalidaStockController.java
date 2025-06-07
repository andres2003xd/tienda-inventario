package com.tiendainventario.controller;

import com.tiendainventario.model.SalidaStock;
import com.tiendainventario.service.SalidaStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salidas-stock")
public class SalidaStockController {

    @Autowired
    private SalidaStockService salidaStockService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarSalida(@RequestBody SalidaStock salida) {
        SalidaStock nuevaSalida = salidaStockService.registrarSalida(salida);

        // Construir respuesta en el formato requerido
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", nuevaSalida.getId());
        respuesta.put("cantidad", nuevaSalida.getCantidad());
        respuesta.put("motivo", nuevaSalida.getMotivo());
        respuesta.put("idVenta", nuevaSalida.getIdVenta());

        // Producto
        if (nuevaSalida.getProducto() != null) {
            Map<String, Object> producto = new LinkedHashMap<>();
            producto.put("id", nuevaSalida.getProducto().getId());
            respuesta.put("producto", producto);
        }

        // Empleado
        if (nuevaSalida.getEmpleado() != null) {
            Map<String, Object> empleado = new LinkedHashMap<>();
            empleado.put("id", nuevaSalida.getEmpleado().getId());
            respuesta.put("empleado", empleado);
        }

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<SalidaStock>> listarTodas() {
        return ResponseEntity.ok(salidaStockService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalidaStock> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(salidaStockService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<SalidaStock>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(salidaStockService.obtenerPorProducto(productoId));
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<SalidaStock>> obtenerPorEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(salidaStockService.obtenerPorEmpleado(empleadoId));
    }

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<SalidaStock>> obtenerPorVenta(@PathVariable Long idVenta) {
        return ResponseEntity.ok(salidaStockService.obtenerPorVenta(idVenta));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<SalidaStock>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(salidaStockService.obtenerPorFechas(inicio, fin));
    }
}