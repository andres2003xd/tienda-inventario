package com.tiendainventario.model;
import com.fasterxml.jackson.annotation.JsonInclude;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;


    public DetalleVenta(Long id, Integer cantidad, Double precioUnitario, Double subtotal, Producto producto, Venta venta) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.producto = producto;
        this.venta = venta;
    }


    //getters

    public Producto getProducto() {
        return producto;
    }

    public Venta getVenta() {
        return venta;
    }

    public Long getId() {
        return id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    //setters
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }


}