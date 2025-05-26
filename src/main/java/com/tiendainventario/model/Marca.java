package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private java.sql.Timestamp fechaCreacion;

    @OneToMany(mappedBy = "marca")
    private List<Producto> productos;

}