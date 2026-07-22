package com.model;

import java.time.LocalDateTime;

public class DiaSemana {

    private int dia;
    private String descripcion;
    private String nombreCorto;
    private String actividad;
    private String usuario;
    private LocalDateTime fechaHora;

    public DiaSemana() {
    }

    public DiaSemana(int dia, String descripcion, String nombreCorto) {
        this.dia = dia;
        this.descripcion = descripcion;
        this.nombreCorto = nombreCorto;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
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