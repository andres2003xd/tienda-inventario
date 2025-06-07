package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.*;
import com.tiendainventario.repository.PqrsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PqrsService extends BaseService<PQRS, Long, PqrsRepository> {

    private final ClienteService clienteService;
    private final TipoPqrsService tipoPqrsService;
    private final EstadoPqrsService estadoPqrsService;
    private final EmpleadoService empleadoService;

    public PqrsService(PqrsRepository repository,
                       ClienteService clienteService,
                       TipoPqrsService tipoPqrsService,
                       EstadoPqrsService estadoPqrsService,
                       EmpleadoService empleadoService) {
        super(repository);
        this.clienteService = clienteService;
        this.tipoPqrsService = tipoPqrsService;
        this.estadoPqrsService = estadoPqrsService;
        this.empleadoService = empleadoService;
    }

    @Transactional
    public PQRS crearPQRS(PQRS pqrs) {
        validarRelaciones(pqrs);
        pqrs.setFechaCreacion(LocalDateTime.now());
        return repository.save(pqrs);
    }

    @Transactional
    public PQRS actualizarPQRS(Long id, PQRS pqrsActualizado) {
        PQRS pqrsExistente = findById(id);
        validarRelaciones(pqrsActualizado);

        pqrsExistente.setCliente(pqrsActualizado.getCliente());
        pqrsExistente.setTipo(pqrsActualizado.getTipo());
        pqrsExistente.setEstado(pqrsActualizado.getEstado());
        pqrsExistente.setTitulo(pqrsActualizado.getTitulo());
        pqrsExistente.setDescripcion(pqrsActualizado.getDescripcion());
        pqrsExistente.setSolucion(pqrsActualizado.getSolucion());

        return repository.save(pqrsExistente);
    }

    @Transactional
    public PQRS cerrarPQRS(Long id, String solucion) {
        PQRS pqrs = findById(id);
        pqrs.setEstado(estadoPqrsService.obtenerPorNombre("Cerrado"));
        pqrs.setFechaCierre(LocalDateTime.now());
        pqrs.setSolucion(solucion);
        return repository.save(pqrs);
    }

    public List<PQRS> obtenerPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public List<PQRS> obtenerPorEstado(Long estadoId) {
        return repository.findByEstadoId(estadoId);
    }

    public List<PQRS> obtenerPorTipo(Long tipoId) {
        return repository.findByTipoId(tipoId);
    }

    private void validarRelaciones(PQRS pqrs) {
        if (!clienteService.existsById(pqrs.getCliente().getId())) {
            throw new ResourceNotFoundException("Cliente", pqrs.getCliente().getId());
        }

        if (!tipoPqrsService.existsById(pqrs.getTipo().getId())) {
            throw new ResourceNotFoundException("TipoPQRS", pqrs.getTipo().getId());
        }

        if (!estadoPqrsService.existsById(pqrs.getEstado().getId())) {
            throw new ResourceNotFoundException("EstadoPQRS", pqrs.getEstado().getId());
        }
    }

    @Override
    protected String getEntityName() {
        return "PQRS";
    }
}