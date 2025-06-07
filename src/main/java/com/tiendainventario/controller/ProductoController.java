package com.tiendainventario.controller;

import com.tiendainventario.model.Producto;
import com.tiendainventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Crear un producto (POST)
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crearProducto(producto);

        // Formatear la respuesta
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", nuevoProducto.getId());
        respuesta.put("nombre", nuevoProducto.getNombre());
        respuesta.put("descripcion", nuevoProducto.getDescripcion());
        respuesta.put("precio", nuevoProducto.getPrecio());

        // Categoria
        if (nuevoProducto.getCategoria() != null) {
            Map<String, Object> categoria = new LinkedHashMap<>();
            categoria.put("id", nuevoProducto.getCategoria().getId());
            respuesta.put("categoria", categoria);
        }

        // Proveedor
        if (nuevoProducto.getProveedor() != null) {
            Map<String, Object> proveedor = new LinkedHashMap<>();
            proveedor.put("id", nuevoProducto.getProveedor().getId());
            respuesta.put("proveedor", proveedor);
        }

        // Marca
        if (nuevoProducto.getMarca() != null) {
            Map<String, Object> marca = new LinkedHashMap<>();
            marca.put("id", nuevoProducto.getMarca().getId());
            respuesta.put("marca", marca);
        }

        // Descuento
        if (nuevoProducto.getDescuento() != null) {
            Map<String, Object> descuento = new LinkedHashMap<>();
            descuento.put("id", nuevoProducto.getDescuento().getId());
            respuesta.put("descuento", descuento);
        }

        return ResponseEntity.ok(respuesta);
    }

    // Obtener un producto por su ID (GET /{id})
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerProducto(@PathVariable Long id) {
        Producto producto = productoService.findById(id);

        // Formatear la respuesta
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", producto.getId());
        respuesta.put("nombre", producto.getNombre());
        respuesta.put("descripcion", producto.getDescripcion());
        respuesta.put("precio", producto.getPrecio());

        // Categoria
        if (producto.getCategoria() != null) {
            Map<String, Object> categoria = new LinkedHashMap<>();
            categoria.put("id", producto.getCategoria().getId());
            respuesta.put("categoria", categoria);
        }

        // Proveedor
        if (producto.getProveedor() != null) {
            Map<String, Object> proveedor = new LinkedHashMap<>();
            proveedor.put("id", producto.getProveedor().getId());
            respuesta.put("proveedor", proveedor);
        }

        // Marca
        if (producto.getMarca() != null) {
            Map<String, Object> marca = new LinkedHashMap<>();
            marca.put("id", producto.getMarca().getId());
            respuesta.put("marca", marca);
        }

        // Descuento
        if (producto.getDescuento() != null) {
            Map<String, Object> descuento = new LinkedHashMap<>();
            descuento.put("id", producto.getDescuento().getId());
            respuesta.put("descuento", descuento);
        }

        return ResponseEntity.ok(respuesta);
    }

    // Obtener todos los productos (GET /)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarProductos() {
        List<Map<String, Object>> productos = productoService.findAll()
                .stream()
                .map(producto -> {
                    Map<String, Object> respuesta = new LinkedHashMap<>();
                    respuesta.put("id", producto.getId());
                    respuesta.put("nombre", producto.getNombre());
                    respuesta.put("descripcion", producto.getDescripcion());
                    respuesta.put("precio", producto.getPrecio());

                    // Categoria
                    if (producto.getCategoria() != null) {
                        Map<String, Object> categoria = new LinkedHashMap<>();
                        categoria.put("id", producto.getCategoria().getId());
                        respuesta.put("categoria", categoria);
                    }

                    // Proveedor
                    if (producto.getProveedor() != null) {
                        Map<String, Object> proveedor = new LinkedHashMap<>();
                        proveedor.put("id", producto.getProveedor().getId());
                        respuesta.put("proveedor", proveedor);
                    }

                    // Marca
                    if (producto.getMarca() != null) {
                        Map<String, Object> marca = new LinkedHashMap<>();
                        marca.put("id", producto.getMarca().getId());
                        respuesta.put("marca", marca);
                    }

                    // Descuento
                    if (producto.getDescuento() != null) {
                        Map<String, Object> descuento = new LinkedHashMap<>();
                        descuento.put("id", producto.getDescuento().getId());
                        respuesta.put("descuento", descuento);
                    }

                    return respuesta;
                })
                .toList();

        return ResponseEntity.ok(productos);
    }
}