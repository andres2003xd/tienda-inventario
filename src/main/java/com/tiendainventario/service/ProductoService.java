package com.tiendainventario.service;

import com.tiendainventario.exception.ProductoAlreadyExistsException;
import com.tiendainventario.exception.ProductoNotFoundException;
import com.tiendainventario.model.Producto;
import com.tiendainventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
    }

    public Producto crearProducto(Producto producto) {
        if (productoRepository.existsByProveedorId(producto.getProveedor().getId()) &&
            productoRepository.existsByCategoriaId(producto.getCategoria().getId())) {
            throw new ProductoAlreadyExistsException(
                "El producto con la misma categoría y proveedor ya existe."
            );
        }
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto producto = buscarPorId(id);
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setStock(productoActualizado.getStock());
        producto.setCategoria(productoActualizado.getCategoria());
        producto.setProveedor(productoActualizado.getProveedor());

        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }
}