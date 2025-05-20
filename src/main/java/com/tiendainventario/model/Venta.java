package com.tiendainventario.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime fecha;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonProperty("cliente")
    private Cliente cliente;

    public Venta(Long id, LocalDateTime fecha, Double total, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        if (cliente != null && cliente.getId() != null) {
            return new Cliente(cliente.getId());
        }
        return null;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}