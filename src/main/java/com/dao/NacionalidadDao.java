package com.dao;



import com.config.Conexion;
import com.model.Nacionalidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NacionalidadDao {

    public List<Nacionalidad> listarTodos() {
        List<Nacionalidad> lista = new ArrayList<>();
        String sql = "SELECT * FROM Nacionalidad";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Nacionalidad n = new Nacionalidad();
                n.setId(rs.getString("id").trim());
                n.setNombre(rs.getString("nombre").trim());
                n.setActividad(rs.getString("actividad"));
                n.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                n.setFechaHora(ts != null ? ts.toLocalDateTime() : null);
                lista.add(n);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar nacionalidades: " + ex.getMessage());
        }

        return lista;
    }
}