package com.tiendainventario.service;

import com.tiendainventario.exception.VentaNotFoundException;
import com.tiendainventario.model.Venta;
import com.tiendainventario.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Venta buscarPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada con ID: " + id));
    }

    public Venta crearVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    public Venta actualizarVenta(Long id, Venta ventaActualizada) {
        Venta venta = buscarPorId(id);

        venta.setFecha(ventaActualizada.getFecha());
        venta.setTotal(ventaActualizada.getTotal());
        venta.setCliente(ventaActualizada.getCliente());
        venta.setDetalles(ventaActualizada.getDetalles());

        return ventaRepository.save(venta);
    }

    public void eliminarVenta(Long id) {
        Venta venta = buscarPorId(id);
        ventaRepository.delete(venta);
    }
}