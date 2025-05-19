package com.tiendainventario.service;

import com.tiendainventario.exception.ClienteAlreadyExistsException;
import com.tiendainventario.exception.ClienteNotFoundException;
import com.tiendainventario.model.Cliente;
import com.tiendainventario.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }


    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));
    }

    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ClienteAlreadyExistsException("El cliente con el email '" + cliente.getEmail() + "' ya existe.");
        }
        return clienteRepository.save(cliente);
    }


    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente cliente = buscarPorId(id);

        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setDireccion(clienteActualizado.getDireccion());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setEmail(clienteActualizado.getEmail());


        if (!cliente.getId().equals(id) && clienteRepository.existsByEmail(clienteActualizado.getEmail())) {
            throw new ClienteAlreadyExistsException("El email '" + clienteActualizado.getEmail() + "' ya está en uso.");
        }

        return clienteRepository.save(cliente);
    }


    public void eliminarCliente(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}