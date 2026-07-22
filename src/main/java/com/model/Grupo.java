package com.model;

import java.time.LocalDateTime;

public class Grupo {

    private String codigoPeriodo;
    private String codigoAsignatura;
    private String numero;
    private int cupo;
    private String horario;

    private String actividad;
    private String usuario;
    private LocalDateTime fechaHora;

    public Grupo() {
    }

    public Grupo(String codigoPeriodo, String codigoAsignatura, String numero, int cupo, String horario) {
        this.codigoPeriodo = codigoPeriodo;
        this.codigoAsignatura = codigoAsignatura;
        this.numero = numero;
        this.cupo = cupo;
        this.horario = horario;
    }

    public String getCodigoPeriodo() {
        return codigoPeriodo;
    }

    public void setCodigoPeriodo(String codigoPeriodo) {
        this.codigoPeriodo = codigoPeriodo;
    }

    public String getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(String codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
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
        return "Grupo{" +
                "codigoPeriodo='" + codigoPeriodo + '\'' +
                ", codigoAsignatura='" + codigoAsignatura + '\'' +
                ", numero='" + numero + '\'' +
                ", cupo=" + cupo +
                ", horario='" + horario + '\'' +
                ", actividad='" + actividad + '\'' +
                ", usuario='" + usuario + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}