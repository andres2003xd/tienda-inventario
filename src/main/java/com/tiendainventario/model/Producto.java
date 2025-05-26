package com.tiendainventario.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    private Double precio;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    @JsonProperty("categoria")
    private Categoria categoria;


    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    @JsonProperty("proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    @JsonProperty("marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_descuento")
    @JsonProperty("descuento")
    private Descuento descuento;

    public Producto(Long id, String nombre, String descripcion, Double precio, Integer stock,
                    Categoria categoria, Proveedor proveedor, Marca marca, Descuento descuento) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.marca = marca;
        this.descuento = descuento;
    }

    public Descuento getDescuento() {
        return descuento;
    }
    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }
    public Marca getMarca() {
        return marca;
    }
    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}