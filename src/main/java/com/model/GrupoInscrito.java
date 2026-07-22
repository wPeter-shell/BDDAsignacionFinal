package com.model;

import java.time.LocalDateTime;

public class GrupoInscrito {

    private String codigoPeriodo;
    private String idEstudiante;
    private String codigoAsignatura;
    private String numeroGrupo;

    private String actividad;
    private String usuario;
    private LocalDateTime fechaHora;

    public GrupoInscrito() {
    }

    public GrupoInscrito(String codigoPeriodo, String idEstudiante, String codigoAsignatura, String numeroGrupo) {
        this.codigoPeriodo = codigoPeriodo;
        this.idEstudiante = idEstudiante;
        this.codigoAsignatura = codigoAsignatura;
        this.numeroGrupo = numeroGrupo;
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

    public String getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(String codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    public String getNumeroGrupo() {
        return numeroGrupo;
    }

    public void setNumeroGrupo(String numeroGrupo) {
        this.numeroGrupo = numeroGrupo;
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