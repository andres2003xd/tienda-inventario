package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "salidas_stock")
@Getter @Setter @NoArgsConstructor
public class SalidaStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    private String motivo;

    @Column(name = "id_venta")
    private Long idVenta;

    // Constructor sin ID para creación
    public SalidaStock(Producto producto, Empleado empleado, Integer cantidad,
                       String motivo, Long idVenta) {
        this.producto = producto;
        this.empleado = empleado;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.idVenta = idVenta;
    }
}