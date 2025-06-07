package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.Empleado;
import com.tiendainventario.repository.CargoEmpleadoRepository;
import com.tiendainventario.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoService extends BaseService<Empleado, Long, EmpleadoRepository> {

    private final CargoEmpleadoRepository cargoRepository;

    public EmpleadoService(EmpleadoRepository repository, CargoEmpleadoRepository cargoRepository) {
        super(repository);
        this.cargoRepository = cargoRepository;
    }
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
    @Override
    protected String getEntityName() {
        return "Empleado";
    }

    public List<Empleado> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Empleado crearEmpleado(Empleado empleado) {
        // Verificar si el documento ya existe
        if (repository.existsByDocumento(empleado.getDocumento())) {
            throw new ResourceAlreadyExistsException("Empleado", "documento", empleado.getDocumento());
        }


        if (empleado.getEmail() != null && repository.existsByEmail(empleado.getEmail())) {
            throw new ResourceAlreadyExistsException("Empleado", "email", empleado.getEmail());
        }


        if (!cargoRepository.existsById(empleado.getCargo().getId())) {
            throw new ResourceNotFoundException("CargoEmpleado", empleado.getCargo().getId());
        }

        return repository.save(empleado);
    }

    @Transactional
    public Empleado actualizarEmpleado(Long id, Empleado empleadoActualizado) {
        Empleado empleado = findById(id);

        // Verificar que el documento no corresponda a otro empleado
        if (!empleado.getDocumento().equals(empleadoActualizado.getDocumento()) &&
                repository.existsByDocumento(empleadoActualizado.getDocumento())) {
            throw new ResourceAlreadyExistsException("Empleado", "documento", empleadoActualizado.getDocumento());
        }

        // Verificar que el email no corresponda a otro empleado
        if (empleadoActualizado.getEmail() != null &&
                !empleadoActualizado.getEmail().equals(empleado.getEmail()) &&
                repository.existsByEmail(empleadoActualizado.getEmail())) {
            throw new ResourceAlreadyExistsException("Empleado", "email", empleadoActualizado.getEmail());
        }

        // Verificar que el cargo existe
        if (!cargoRepository.existsById(empleadoActualizado.getCargo().getId())) {
            throw new ResourceNotFoundException("CargoEmpleado", empleadoActualizado.getCargo().getId());
        }

        empleado.setCargo(empleadoActualizado.getCargo());
        empleado.setNombre(empleadoActualizado.getNombre());
        empleado.setApellido(empleadoActualizado.getApellido());
        empleado.setDocumento(empleadoActualizado.getDocumento());
        empleado.setTelefono(empleadoActualizado.getTelefono());
        empleado.setEmail(empleadoActualizado.getEmail());
        empleado.setFechaContratacion(empleadoActualizado.getFechaContratacion());

        return repository.save(empleado);
    }
}