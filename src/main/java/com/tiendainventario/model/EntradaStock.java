package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "entradas_stock")
@Getter @Setter @NoArgsConstructor
public class EntradaStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    private String motivo;

    @Column(name = "numero_factura")
    private String numeroFactura;

    // Constructor sin ID para creación
    public EntradaStock(Producto producto, Proveedor proveedor, Empleado empleado,
                        Integer cantidad, String motivo, String numeroFactura) {
        this.producto = producto;
        this.proveedor = proveedor;
        this.empleado = empleado;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.numeroFactura = numeroFactura;
    }
}