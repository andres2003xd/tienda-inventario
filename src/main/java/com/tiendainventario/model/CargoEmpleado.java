package com.tiendainventario.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cargo_empleado")  // Asegúrate que coincida con el nombre en la BD
public class CargoEmpleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "salario_base", nullable = false)
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