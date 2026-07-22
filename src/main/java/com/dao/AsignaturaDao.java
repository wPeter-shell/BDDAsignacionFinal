package com.dao;

import Logic.Asignatura;
import Conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AsignaturaDao {

    public List<Asignatura> listarTodos() {
        List<Asignatura> lista = new ArrayList<>();
        String sql = "SELECT * FROM Asignatura";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Asignatura a = new Asignatura();
                a.setCodigo(rs.getString("codigo").trim());
                a.setNombre(rs.getString("nombre").trim());
                a.setCreditos(rs.getInt("creditos"));
                a.setHorasTeoricas(rs.getInt("horas_teoricas"));
                a.setHorasPracticas(rs.getInt("horas_practicas"));
                a.setActividad(rs.getString("actividad"));
                a.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                a.setFechaHora(ts != null ? ts.toLocalDateTime() : null);
                lista.add(a);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar asignaturas: " + ex.getMessage());
        }

        return lista;
    }

    public boolean insertar(Asignatura a) {
        String sql = "INSERT INTO Asignatura (codigo, nombre, creditos, horas_teoricas, horas_practicas) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getCodigo());
            ps.setString(2, a.getNombre());
            ps.setInt(3, a.getCreditos());
            ps.setInt(4, a.getHorasTeoricas());
            ps.setInt(5, a.getHorasPracticas());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al insertar asignatura: " + ex.getMessage());
            return false;
        }
    }

    public boolean actualizar(Asignatura a) {
        String sql = "UPDATE Asignatura SET nombre = ?, creditos = ?, horas_teoricas = ?, horas_practicas = ? WHERE codigo = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getNombre());
            ps.setInt(2, a.getCreditos());
            ps.setInt(3, a.getHorasTeoricas());
            ps.setInt(4, a.getHorasPracticas());
            ps.setString(5, a.getCodigo());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al actualizar asignatura: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(String codigo) {
        String sql = "DELETE FROM Asignatura WHERE codigo = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al eliminar asignatura: " + ex.getMessage());
            return false;
        }
    }
}