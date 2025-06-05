package com.tiendainventario.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "historial-login") // Mapeo al nombre de la tabla en la base de datos
public class HistorialLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_login")
    private Long id; // Corresponde a "id_login"

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario; // Relación con el usuario

    @Column(name = "fecha_login", nullable = false)
    private LocalDateTime fechaLogin;

    @Column(nullable = false, length = 45)
    private String ip;

    @Column(length = 255)
    private String dispositivo;



    public HistorialLogin(Long id, Usuario usuario, LocalDateTime fechaLogin, String ip, String dispositivo) {
        this.id = id;
        this.usuario = usuario;
        this.fechaLogin = fechaLogin;
        this.ip = ip;
        this.dispositivo = dispositivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaLogin() {
        return fechaLogin;
    }

    public void setFechaLogin(LocalDateTime fechaLogin) {
        this.fechaLogin = fechaLogin;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }
}