package com.tiendainventario.service;

import com.tiendainventario.exception.DetalleVentaAlreadyExistsException;
import com.tiendainventario.exception.DetalleVentaNotFoundException;
import com.tiendainventario.model.DetalleVenta;
import com.tiendainventario.model.Producto;
import com.tiendainventario.repository.DetalleVentaRepository;
import com.tiendainventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private ProductoRepository productoRepository;


    public List<DetalleVenta> listarDetallesVenta() {
        return detalleVentaRepository.findAll();
    }

    public DetalleVenta buscarPorId(Long id) {
        return detalleVentaRepository.findById(id)
                .orElseThrow(() -> new DetalleVentaNotFoundException("Detalle de venta no encontrado con ID: " + id));
    }


    public DetalleVenta crearDetalleVenta(DetalleVenta detalleVenta) {
        // Verificar que exista un producto con el ID proporcionado
        Long productoId = detalleVenta.getProducto().getId();
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("El producto con ID " + productoId + " no existe."));

        // Validar si el producto tiene proveedor asociado
        if (producto.getProveedor() == null) {
            throw new IllegalArgumentException("El producto debe tener un proveedor asociado.");
        }

        // Actualizar la referencia del producto en el detalle de venta
        detalleVenta.setProducto(producto);

        // Persistir el detalle de venta
        return detalleVentaRepository.save(detalleVenta);
    }

    public DetalleVenta actualizarDetalleVenta(Long id, DetalleVenta detalleActualizado) {
        DetalleVenta detalleExistente = buscarPorId(id);
        detalleExistente.setCantidad(detalleActualizado.getCantidad());
        detalleExistente.setPrecioUnitario(detalleActualizado.getPrecioUnitario());
        detalleExistente.setSubtotal(detalleActualizado.getSubtotal());
        detalleExistente.setProducto(detalleActualizado.getProducto());
        detalleExistente.setVenta(detalleActualizado.getVenta());
        
        return detalleVentaRepository.save(detalleExistente);
    }

    public void eliminarDetalleVenta(Long id) {
        DetalleVenta detalle = buscarPorId(id);
        detalleVentaRepository.delete(detalle);
    }
}