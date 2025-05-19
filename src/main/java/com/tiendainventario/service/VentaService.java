package com.tiendainventario.service;

import com.tiendainventario.exception.VentaNotFoundException;
import com.tiendainventario.model.Cliente;
import com.tiendainventario.model.Venta;
import com.tiendainventario.repository.ClienteRepository;
import com.tiendainventario.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Método que lista todas las ventas
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    // Método que busca una venta por su ID
    public Venta buscarPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada con ID: " + id));
    }

    // Lógica para crear una venta
    public Venta crearVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    // Actualización de una venta existente
    public Venta actualizarVenta(Long id, Venta ventaActualizada) {
        Venta venta = buscarPorId(id);

        venta.setFecha(ventaActualizada.getFecha());
        venta.setTotal(ventaActualizada.getTotal());
        venta.setCliente(ventaActualizada.getCliente());
        venta.setDetalles(ventaActualizada.getDetalles());

        return ventaRepository.save(venta);
    }

    // Método para validar que un cliente existe en la base de datos
    public Cliente obtenerClientePorId(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("No se encontró el cliente con ID: " + clienteId));
    }

    // Guardado de una lógica común para eliminar ventas
    public void eliminarVenta(Long id) {
        Venta venta = buscarPorId(id);
        ventaRepository.delete(venta);
    }
}