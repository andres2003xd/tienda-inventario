package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CARGO_EMPLEADO")
@Getter
@Setter
@NoArgsConstructor
public class CargoEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARGO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, unique = true)
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "SALARIO_BASE", nullable = false)
    private Double salarioBase;

    public CargoEmpleado(Long id, String nombre, String descripcion, Double salarioBase) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.salarioBase = salarioBase;
    }

    public CargoEmpleado orElseThrow(Object o) {
        return null;
    }
}