package com.dao;


import com.config.Conexion;
import com.model.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDao {

    public List<Estudiante> listarTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM Estudiante";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estudiante e = new Estudiante();
                e.setId(rs.getString("id").trim());
                e.setNombre(rs.getString("nombre").trim());
                e.setApellio(rs.getString("apellio").trim());
                e.setIdCarrera(rs.getString("id_carrera").trim());
                e.setIdCategoriaPago(rs.getString("id_categoria_pago").trim());
                e.setIdNacionalidad(rs.getString("id_nacionalidad").trim());
                e.setDireccion(rs.getString("direccion") != null ? rs.getString("direccion").trim() : null);

                e.setActividad(rs.getString("actividad"));
                e.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                e.setFechaHora(ts != null ? ts.toLocalDateTime() : null);

                lista.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar estudiantes: " + ex.getMessage());
        }

        return lista;
    }

    public boolean insertar(Estudiante e) {
        // No se incluyen actividad/usuario/fechahora: los llena el trigger automaticamente
        String sql = "INSERT INTO Estudiante (id, nombre, apellio, id_carrera, id_categoria_pago, id_nacionalidad, direccion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getId());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getApellio());
            ps.setString(4, e.getIdCarrera());
            ps.setString(5, e.getIdCategoriaPago());
            ps.setString(6, e.getIdNacionalidad());
            ps.setString(7, e.getDireccion());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al insertar estudiante: " + ex.getMessage());
            return false;
        }
    }

    public boolean actualizar(Estudiante e) {

        String sql = "UPDATE Estudiante SET nombre = ?, apellio = ?, id_carrera = ?, "
                + "id_categoria_pago = ?, id_nacionalidad = ?, direccion = ? "
                + "WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellio());
            ps.setString(3, e.getIdCarrera());
            ps.setString(4, e.getIdCategoriaPago());
            ps.setString(5, e.getIdNacionalidad());
            ps.setString(6, e.getDireccion());
            ps.setString(7, e.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al actualizar estudiante: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(String id) {
        String sql = "DELETE FROM Estudiante WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al eliminar estudiante: " + ex.getMessage());
            return false;
        }
    }

    public String obtenerSiguienteId() {
        String sql = "SELECT ISNULL(MAX(CAST(id AS BIGINT)), 10150000) + 1 AS siguiente "
                + "FROM Estudiante WHERE id NOT LIKE '%[^0-9]%'";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                long siguiente = rs.getLong("siguiente");
                return String.format("%08d", siguiente);
            }

        } catch (SQLException ex) {
            System.out.println("Error al obtener siguiente id: " + ex.getMessage());
        }

        return null;
    }

    public List<String> obtenerDependencias(String id) {
        List<String> razones = new ArrayList<>();

        String sqlInscripcion = "SELECT COUNT(*) AS total FROM Inscripcion WHERE id_estudiante = ?";
        String sqlGrupoInscrito = "SELECT COUNT(*) AS total FROM Grupo_Inscrito WHERE id_estudiante = ?";

        try (Connection con = Conexion.conectar()) {

            try (PreparedStatement ps = con.prepareStatement(sqlInscripcion)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt("total") > 0) {
                        razones.add("Tiene " + rs.getInt("total") + " inscripcion(es) de periodo registrada(s).");
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlGrupoInscrito)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt("total") > 0) {
                        razones.add("Tiene " + rs.getInt("total") + " grupo(s) inscrito(s).");
                    }
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al verificar dependencias del estudiante: " + ex.getMessage());
        }

        return razones;
    }

}