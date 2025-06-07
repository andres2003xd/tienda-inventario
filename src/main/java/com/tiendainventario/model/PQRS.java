package com.tiendainventario.model;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "PQRS")
public class PQRS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PQRS")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_PQRS", nullable = false)
    private TipoPQRS tipo;

    @ManyToOne
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    private EstadoPQRS estado;


    @Column(name = "TITULO", nullable = false)
    private String titulo;

    @Column(name = "DESCRIPCION", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "FECHA_CREACION", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "FECHA_CIERRE")
    private LocalDateTime fechaCierre;

    @JsonIgnore
    @Column(name = "SOLUCION", columnDefinition = "TEXT")
    private String solucion;


    public PQRS(Cliente cliente, TipoPQRS tipo, EstadoPQRS estado,
                String titulo, String descripcion) {
        this.cliente = cliente;
        this.tipo = tipo;
        this.estado = estado;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoPQRS getEstado() {
        return estado;
    }

    public void setEstado(EstadoPQRS estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoPQRS getTipo() {
        return tipo;
    }

    public void setTipo(TipoPQRS tipo) {
        this.tipo = tipo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}