package com.dao;


import com.config.Conexion;
import com.model.DiaSemana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DiaSemanaDao {

    public List<DiaSemana> listarTodos() {
        List<DiaSemana> lista = new ArrayList<>();
        String sql = "SELECT * FROM Dia_Semana ORDER BY dia";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DiaSemana d = new DiaSemana();
                d.setDia(rs.getInt("dia"));
                d.setDescripcion(rs.getString("descripcion").trim());
                d.setNombreCorto(rs.getString("nombre_corto").trim());
                d.setActividad(rs.getString("actividad"));
                d.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                d.setFechaHora(ts != null ? ts.toLocalDateTime() : null);
                lista.add(d);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar dias de la semana: " + ex.getMessage());
        }

        return lista;
    }
}