package com.tiendainventario.service;

import com.tiendainventario.model.Venta;
import com.tiendainventario.repository.ClienteRepository;
import com.tiendainventario.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Crear nueva venta
    public Venta crearVenta(Map<String, Object> requestBody) {
        // Lógica para crear una venta
        return new Venta(); // Implementar la lógica aquí
    }

    // Listar todas las ventas
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    // Obtener una venta por ID
    public Optional<Venta> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    // Actualizar una venta
    public Venta actualizarVenta(Long id, Map<String, Object> requestBody) {
        // Lógica para actualizar una venta
        return new Venta(); // Implementar la lógica aquí
    }

    // Eliminar una venta por ID
    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }
}