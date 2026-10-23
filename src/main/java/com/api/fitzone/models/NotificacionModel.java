package com.api.fitzone.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificacion")
public class NotificacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_socio", nullable = false)
    private SocioModel socio;

    @Column(nullable = false, length = 255)
    private String mensaje;

    @Column(nullable = false, length = 30)
    private String tipo; // CONFIRMACION, RECORDATORIO

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    public NotificacionModel() {
    }

    public NotificacionModel(SocioModel socio, String mensaje, String tipo, LocalDateTime fechaEnvio) {
        this.socio = socio;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fechaEnvio = fechaEnvio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SocioModel getSocio() {
        return socio;
    }

    public void setSocio(SocioModel socio) {
        this.socio = socio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}