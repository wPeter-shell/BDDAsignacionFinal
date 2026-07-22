package com.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Estudiante {

    private String id;
    private String nombre;
    private String apellio;
    private String idCarrera;
    private String idCategoriaPago;
    private String idNacionalidad;
    private String direccion;
    private String actividad;
    private String usuario;
    private LocalDateTime fechaHora;

    public Estudiante() {
    }

    public Estudiante(String id, String nombre, String apellio, String idCarrera,
                      String idCategoriaPago, String idNacionalidad, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellio = apellio;
        this.idCarrera = idCarrera;
        this.idCategoriaPago = idCategoriaPago;
        this.idNacionalidad = idNacionalidad;
        this.direccion = direccion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellio() {
        return apellio;
    }

    public void setApellio(String apellio) {
        this.apellio = apellio;
    }

    public String getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(String idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getIdCategoriaPago() {
        return idCategoriaPago;
    }

    public void setIdCategoriaPago(String idCategoriaPago) {
        this.idCategoriaPago = idCategoriaPago;
    }

    public String getIdNacionalidad() {
        return idNacionalidad;
    }

    public void setIdNacionalidad(String idNacionalidad) {
        this.idNacionalidad = idNacionalidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
        return "Estudiante{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellio='" + apellio + '\'' +
                ", idCarrera='" + idCarrera + '\'' +
                ", idCategoriaPago='" + idCategoriaPago + '\'' +
                ", idNacionalidad='" + idNacionalidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", actividad='" + actividad + '\'' +
                ", usuario='" + usuario + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}