package com.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PeriodoAcademico {

    private String codigo;
    private String descripcion;
    private LocalDate fechaLimitePrematricula;
    private LocalDate fechaLimiteRetiro;
    private LocalDate fechaLimitePublicacion;
    private String actividad;
    private String usuario;
    private LocalDateTime fechaHora;

    public PeriodoAcademico() {
    }

    public PeriodoAcademico(String codigo, String descripcion, LocalDate fechaLimitePrematricula,
                            LocalDate fechaLimiteRetiro, LocalDate fechaLimitePublicacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.fechaLimitePrematricula = fechaLimitePrematricula;
        this.fechaLimiteRetiro = fechaLimiteRetiro;
        this.fechaLimitePublicacion = fechaLimitePublicacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaLimitePrematricula() {
        return fechaLimitePrematricula;
    }

    public void setFechaLimitePrematricula(LocalDate fechaLimitePrematricula) {
        this.fechaLimitePrematricula = fechaLimitePrematricula;
    }

    public LocalDate getFechaLimiteRetiro() {
        return fechaLimiteRetiro;
    }

    public void setFechaLimiteRetiro(LocalDate fechaLimiteRetiro) {
        this.fechaLimiteRetiro = fechaLimiteRetiro;
    }

    public LocalDate getFechaLimitePublicacion() {
        return fechaLimitePublicacion;
    }

    public void setFechaLimitePublicacion(LocalDate fechaLimitePublicacion) {
        this.fechaLimitePublicacion = fechaLimitePublicacion;
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

    @Override
    public String toString() {
        return descripcion;
    }
}