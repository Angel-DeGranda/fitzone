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
@Table(name = "inscripcion")
public class InscripcionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folio_inscripcion")
    private Long folioInscripcion;

    @ManyToOne
    @JoinColumn(name = "id_socio", nullable = false)
    private SocioModel socio;

    @ManyToOne
    @JoinColumn(name = "id_clase", nullable = false)
    private ClaseModel clase;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false, length = 20)
    private String estatus; // CONFIRMADA, CANCELADA

    public InscripcionModel() {
    }

    public InscripcionModel(SocioModel socio, ClaseModel clase, LocalDateTime fechaRegistro, String estatus) {
        this.socio = socio;
        this.clase = clase;
        this.fechaRegistro = fechaRegistro;
        this.estatus = estatus;
    }

    public Long getFolioInscripcion() {
        return folioInscripcion;
    }

    public void setFolioInscripcion(Long folioInscripcion) {
        this.folioInscripcion = folioInscripcion;
    }

    public SocioModel getSocio() {
        return socio;
    }

    public void setSocio(SocioModel socio) {
        this.socio = socio;
    }

    public ClaseModel getClase() {
        return clase;
    }

    public void setClase(ClaseModel clase) {
        this.clase = clase;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}