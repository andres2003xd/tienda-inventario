package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;


    public Venta(Long id, LocalDateTime fecha, Double total, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.cliente = cliente;
    }

    //setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    //getters
    public Long getId() {
        return id;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public Double getTotal() {
        return total;
    }
    public Cliente getCliente() {
        return cliente;
    }
}