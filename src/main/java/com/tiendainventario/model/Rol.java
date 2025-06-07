package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;

    @Column(name = "TIPO_ROL", length = 255)
    private String tipoRol; // Nuevo atributo mapeado a la columna TIPO_ROL

    @Column(length = 255, nullable = false, unique = true)
    private String descripcion;

    public Rol(Long id, String tipoRol, String descripcion) {
        this.id = id;
        this.tipoRol = tipoRol;
        this.descripcion = descripcion;
    }

    public Rol(String tipoRol, String descripcion) {
        this.tipoRol = tipoRol;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(String tipoRol) {
        this.tipoRol = tipoRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}