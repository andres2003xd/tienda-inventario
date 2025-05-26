package com.tiendainventario.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CargoEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Double salarioBase;

    @OneToMany(mappedBy = "cargo")
    @JsonProperty("empleados")
    private List<Empleado> empleados;


    public CargoEmpleado(Long id, String nombre, String descripcion, Double salarioBase) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.salarioBase = salarioBase;
    }

}