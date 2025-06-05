package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_producto", nullable = false, unique = true)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private String ubicacion;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "stock_maximo")
    private Integer stockMaximo;

    public Stock(Producto producto, Integer cantidad, String ubicacion, Integer stockMinimo, Integer stockMaximo) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
    }

}