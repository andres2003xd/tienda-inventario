package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CARGO", nullable = false)
    private CargoEmpleado cargo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 20, unique = true)
    private String documento;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(name = "FECHA_CONTRATACION", nullable = false)
    private LocalDate fechaContratacion;

    @OneToMany(mappedBy = "empleado")
    private List<Rol> roles;
}