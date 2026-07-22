package com.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Inscripcion {

    private String codigoPeriodo;
    private String idEstudiante;
    private LocalDate fechaInscripcion;

    private String actividad;
    private String usuario;
    private LocalDateTime fechaHora;

    public Inscripcion() {
    }

    public Inscripcion(String codigoPeriodo, String idEstudiante, LocalDate fechaInscripcion) {
        this.codigoPeriodo = codigoPeriodo;
        this.idEstudiante = idEstudiante;
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getCodigoPeriodo() {
        return codigoPeriodo;
    }

    public void setCodigoPeriodo(String codigoPeriodo) {
        this.codigoPeriodo = codigoPeriodo;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}