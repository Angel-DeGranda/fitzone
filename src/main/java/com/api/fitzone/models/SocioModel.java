package com.api.fitzone.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "socio")
public class SocioModel {

    @Id
    @Column(name = "id_socio", length = 10)
    private String idSocio;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(name = "membresia_vigente", nullable = false)
    private boolean membresiaVigente;

    @JsonIgnore
    @OneToMany(mappedBy = "socio")
    private List<InscripcionModel> inscripciones = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "socio")
    private List<NotificacionModel> notificaciones = new ArrayList<>();

    public SocioModel() {
    }

    public SocioModel(String idSocio, String nombre, String correo, boolean membresiaVigente) {
        this.idSocio = idSocio;
        this.nombre = nombre;
        this.correo = correo;
        this.membresiaVigente = membresiaVigente;
    }

    public String getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(String idSocio) {
        this.idSocio = idSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isMembresiaVigente() {
        return membresiaVigente;
    }

    public void setMembresiaVigente(boolean membresiaVigente) {
        this.membresiaVigente = membresiaVigente;
    }

    public List<InscripcionModel> getInscripciones() {
        return inscripciones;
    }

    public List<NotificacionModel> getNotificaciones() {
        return notificaciones;
    }
}