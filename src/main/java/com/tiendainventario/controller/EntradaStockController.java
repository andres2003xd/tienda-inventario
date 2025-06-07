package com.tiendainventario.controller;

import com.tiendainventario.model.EntradaStock;
import com.tiendainventario.service.EntradaStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entradas-stock")
public class EntradaStockController {

    @Autowired
    private EntradaStockService entradaStockService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarEntrada(@RequestBody EntradaStock entrada) {
        EntradaStock nuevaEntrada = entradaStockService.registrarEntrada(entrada);

        // Construir respuesta en el formato requerido
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", nuevaEntrada.getId());
        respuesta.put("cantidad", nuevaEntrada.getCantidad());
        respuesta.put("motivo", nuevaEntrada.getMotivo());
        respuesta.put("numeroFactura", nuevaEntrada.getNumeroFactura());

        // Producto
        if (nuevaEntrada.getProducto() != null) {
            Map<String, Object> producto = new LinkedHashMap<>();
            producto.put("id", nuevaEntrada.getProducto().getId());
            respuesta.put("producto", producto);
        }

        // Proveedor
        if (nuevaEntrada.getProveedor() != null) {
            Map<String, Object> proveedor = new LinkedHashMap<>();
            proveedor.put("id", nuevaEntrada.getProveedor().getId());
            respuesta.put("proveedor", proveedor);
        }

        // Empleado
        if (nuevaEntrada.getEmpleado() != null) {
            Map<String, Object> empleado = new LinkedHashMap<>();
            empleado.put("id", nuevaEntrada.getEmpleado().getId());
            respuesta.put("empleado", empleado);
        }

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<EntradaStock>> listarTodas() {
        return ResponseEntity.ok(entradaStockService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaStock> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entradaStockService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<EntradaStock>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(entradaStockService.obtenerPorProducto(productoId));
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<EntradaStock>> obtenerPorProveedor(@PathVariable Long proveedorId) {
        return ResponseEntity.ok(entradaStockService.obtenerPorProveedor(proveedorId));
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<EntradaStock>> obtenerPorEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(entradaStockService.obtenerPorEmpleado(empleadoId));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<EntradaStock>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(entradaStockService.obtenerPorFechas(inicio, fin));
    }
}