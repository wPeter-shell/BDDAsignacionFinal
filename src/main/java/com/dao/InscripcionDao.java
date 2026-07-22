package com.dao;



import com.config.Conexion;
import com.model.Inscripcion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDao {

    public List<Inscripcion> listarTodos() {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Inscripcion";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Inscripcion i = new Inscripcion();
                i.setCodigoPeriodo(rs.getString("codigo_periodo").trim());
                i.setIdEstudiante(rs.getString("id_estudiante").trim());
                i.setFechaInscripcion(rs.getDate("fecha_inscripcion").toLocalDate());
                i.setActividad(rs.getString("actividad"));
                i.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                i.setFechaHora(ts != null ? ts.toLocalDateTime() : null);
                lista.add(i);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar inscripciones: " + ex.getMessage());
        }

        return lista;
    }

    public boolean existe(String codigoPeriodo, String idEstudiante) {
        String sql = "SELECT 1 FROM Inscripcion WHERE codigo_periodo = ? AND id_estudiante = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);
            ps.setString(2, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            System.out.println("Error al verificar inscripcion: " + ex.getMessage());
            return false;
        }
    }

    public boolean insertar(Inscripcion i) {
        String sql = "INSERT INTO Inscripcion (codigo_periodo, id_estudiante, fecha_inscripcion) VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, i.getCodigoPeriodo());
            ps.setString(2, i.getIdEstudiante());
            ps.setDate(3, Date.valueOf(i.getFechaInscripcion()));

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al insertar inscripcion: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(String codigoPeriodo, String idEstudiante) {
        String sql = "DELETE FROM Inscripcion WHERE codigo_periodo = ? AND id_estudiante = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);
            ps.setString(2, idEstudiante);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al eliminar inscripcion: " + ex.getMessage());
            return false;
        }
    }
}