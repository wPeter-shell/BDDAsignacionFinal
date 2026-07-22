package com.dao;

import Logic.Carrera;
import Conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CarreraDao {

    public List<Carrera> listarTodos() {
        List<Carrera> lista = new ArrayList<>();
        String sql = "SELECT * FROM Carrera";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Carrera c = new Carrera();
                c.setId(rs.getString("id").trim());
                c.setNombre(rs.getString("nombre").trim());
                c.setActividad(rs.getString("actividad"));
                c.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                c.setFechaHora(ts != null ? ts.toLocalDateTime() : null);
                lista.add(c);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar carreras: " + ex.getMessage());
        }

        return lista;
    }
}