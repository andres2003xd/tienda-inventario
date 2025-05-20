package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.model.Cliente;
import com.tiendainventario.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends BaseService<Cliente, Long, ClienteRepository> {

    public ClienteService(ClienteRepository repository) {
        super(repository);
    }

    public Cliente crearCliente(Cliente cliente) {
        if (repository.existsByEmail(cliente.getEmail())) {
            throw new ResourceAlreadyExistsException("Cliente", "email", cliente.getEmail());
        }
        return repository.save(cliente);
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente cliente = findById(id);
        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setDireccion(clienteActualizado.getDireccion());
        cliente.setEmail(clienteActualizado.getEmail());
        cliente.setTelefono(clienteActualizado.getTelefono());
        return repository.save(cliente);
    }

    @Override
    protected String getEntityName() {
        return "Cliente";
    }
}