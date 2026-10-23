package com.api.fitzone.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "clase")
public class ClaseModel {

    @Id
    @Column(name = "id_clase", length = 10)
    private String idClase;

    @Column(nullable = false, length = 50)
    private String disciplina; // Yoga, Spinning, Funcional

    @Column(nullable = false, length = 50)
    private String sucursal;

    @Column(name = "cupo_maximo", nullable = false)
    private int cupoMaximo = 20;

    @Column(name = "cupo_ocupado", nullable = false)
    private int cupoOcupado = 0;

    @Column(name = "fecha_clase", nullable = false)
    private LocalDate fechaClase;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @JsonIgnore
    @OneToMany(mappedBy = "clase")
    private List<InscripcionModel> inscripciones = new ArrayList<>();

    public ClaseModel() {
    }

    public ClaseModel(String idClase, String disciplina, String sucursal, int cupoMaximo, int cupoOcupado, LocalDate fechaClase, LocalTime horaInicio) {
        this.idClase = idClase;
        this.disciplina = disciplina;
        this.sucursal = sucursal;
        this.cupoMaximo = cupoMaximo;
        this.cupoOcupado = cupoOcupado;
        this.fechaClase = fechaClase;
        this.horaInicio = horaInicio;
    }

    public String getIdClase() {
        return idClase;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public int getCupoOcupado() {
        return cupoOcupado;
    }

    public void setCupoOcupado(int cupoOcupado) {
        this.cupoOcupado = cupoOcupado;
    }

    public LocalDate getFechaClase() {
        return fechaClase;
    }

    public void setFechaClase(LocalDate fechaClase) {
        this.fechaClase = fechaClase;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public List<InscripcionModel> getInscripciones() {
        return inscripciones;
    }

    public int getCupoDisponible() {
        return this.cupoMaximo - this.cupoOcupado;
    }
}