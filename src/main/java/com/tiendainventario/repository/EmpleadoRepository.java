// Empleado.java
package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "EMPLEADO")
@Getter
@Setter
@NoArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CARGO", nullable = false)
    private CargoEmpleado cargo;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    @Column(name = "DOCUMENTO", nullable = false, unique = true)
    private String documento;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "FECHA_CONTRATACION", nullable = false)
    private LocalDate fechaContratacion;

    public Empleado(Long id, CargoEmpleado cargo, String nombre, String apellido,
                    String documento, String telefono, String email, LocalDate fechaContratacion) {
        this.id = id;
        this.cargo = cargo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.fechaContratacion = fechaContratacion;
    }
}