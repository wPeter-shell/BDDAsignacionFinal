package com.dao;

import Logic.PeriodoAcademico;
import Conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PeriodoAcademicoDao {

    public List<PeriodoAcademico> listarTodos() {
        List<PeriodoAcademico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Periodo_Academico";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PeriodoAcademico p = new PeriodoAcademico();
                p.setCodigo(rs.getString("codigo").trim());
                p.setDescripcion(rs.getString("descripcion").trim());
                p.setFechaLimitePrematricula(rs.getDate("fecha_limite_prematricula").toLocalDate());
                p.setFechaLimiteRetiro(rs.getDate("fecha_limite_retiro").toLocalDate());
                p.setFechaLimitePublicacion(rs.getDate("fecha_limite_publicacion").toLocalDate());
                p.setActividad(rs.getString("actividad"));
                p.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                p.setFechaHora(ts != null ? ts.toLocalDateTime() : null);
                lista.add(p);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar periodos academicos: " + ex.getMessage());
        }

        return lista;
    }
}