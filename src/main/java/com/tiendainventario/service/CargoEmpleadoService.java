package com.tiendainventario.service;


import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.model.CargoEmpleado;
import com.tiendainventario.repository.CargoEmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CargoEmpleadoService extends BaseService<CargoEmpleado, Long, CargoEmpleadoRepository> {

    public CargoEmpleadoService(CargoEmpleadoRepository repository) {
        super(repository);
    }

    @Transactional
    public CargoEmpleado crearCargo(CargoEmpleado cargo) {
        if (repository.existsByNombre(cargo.getNombre())) {
            throw new ResourceAlreadyExistsException("CargoEmpleado", "nombre", cargo.getNombre());
        }
        return repository.save(cargo);
    }

    @Transactional
    public CargoEmpleado actualizarCargo(Long id, CargoEmpleado cargoActualizado) {
        CargoEmpleado cargo = findById(id);

        // Verificar si el nuevo nombre ya existe (y no es el mismo cargo)
        if (!cargo.getNombre().equals(cargoActualizado.getNombre()) &&
                repository.existsByNombre(cargoActualizado.getNombre())) {
            throw new ResourceAlreadyExistsException("CargoEmpleado", "nombre", cargoActualizado.getNombre());
        }

        cargo.setNombre(cargoActualizado.getNombre());
        cargo.setDescripcion(cargoActualizado.getDescripcion());
        cargo.setSalarioBase(cargoActualizado.getSalarioBase());

        return repository.save(cargo);
    }

    public CargoEmpleado obtenerCargoPorNombre(String nombre) {
        return repository.findByNombre(nombre)
                .orElseThrow((java.util.function.Supplier<ResourceNotFoundException>) () -> new ResourceNotFoundException("CargoEmpleado", "nombre", nombre));
    }

    @Override
    protected String getEntityName() {
        return "CargoEmpleado";
    }
}