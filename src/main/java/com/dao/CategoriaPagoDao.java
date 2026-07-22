package com.dao;



import com.config.Conexion;
import com.model.CategoriaPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CategoriaPagoDao {

    public List<CategoriaPago> listarTodos() {
        List<CategoriaPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Categoria_Pago";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoriaPago c = new CategoriaPago();
                c.setId(rs.getString("id").trim());
                c.setDescripcion(rs.getString("descripcion").trim());
                c.setActividad(rs.getString("actividad"));
                c.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                c.setFechaHora(ts != null ? ts.toLocalDateTime() : null);
                lista.add(c);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar categorias de pago: " + ex.getMessage());
        }

        return lista;
    }
}