package com.tiendainventario.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Excluir campos nulos
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    private Double precio;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonProperty("categoria") // Personaliza el nombre del campo en JSON
    private Categoria categoria;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id")
    //@JsonProperty("proveedor") // Personaliza el nombre del campo en JSON
    private Proveedor proveedor;

    public Producto(Long id, String nombre, String descripcion, Double precio, Integer stock, Proveedor proveedor, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.proveedor = proveedor;
        this.categoria = categoria;
    }
}
