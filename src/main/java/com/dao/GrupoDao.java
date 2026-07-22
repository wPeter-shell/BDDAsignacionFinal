package com.dao;

import Logic.Grupo;
import Conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GrupoDao {

    public List<Grupo> listarTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Grupo";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearGrupo(rs));
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar grupos: " + ex.getMessage());
        }

        return lista;
    }

    public List<Grupo> listarPorPeriodo(String codigoPeriodo) {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Grupo WHERE codigo_periodo = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearGrupo(rs));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar grupos por periodo: " + ex.getMessage());
        }

        return lista;
    }

    public boolean insertar(Grupo g) {
        String sql = "INSERT INTO Grupo (codigo_periodo, codigo_asignatura, numero, cupo, horario) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, g.getCodigoPeriodo());
            ps.setString(2, g.getCodigoAsignatura());
            ps.setString(3, g.getNumero());
            ps.setInt(4, g.getCupo());
            ps.setString(5, g.getHorario());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al insertar grupo: " + ex.getMessage());
            return false;
        }
    }

    public boolean actualizarCupo(String codigoPeriodo, String codigoAsignatura, String numero, int nuevoCupo) {
        String sql = "UPDATE Grupo SET cupo = ? WHERE codigo_periodo = ? AND codigo_asignatura = ? AND numero = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoCupo);
            ps.setString(2, codigoPeriodo);
            ps.setString(3, codigoAsignatura);
            ps.setString(4, numero);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al actualizar cupo del grupo: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(String codigoPeriodo, String codigoAsignatura, String numero) {
        String sql = "DELETE FROM Grupo WHERE codigo_periodo = ? AND codigo_asignatura = ? AND numero = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);
            ps.setString(2, codigoAsignatura);
            ps.setString(3, numero);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al eliminar grupo: " + ex.getMessage());
            return false;
        }
    }

    private Grupo mapearGrupo(ResultSet rs) throws SQLException {
        Grupo g = new Grupo();
        g.setCodigoPeriodo(rs.getString("codigo_periodo").trim());
        g.setCodigoAsignatura(rs.getString("codigo_asignatura").trim());
        g.setNumero(rs.getString("numero").trim());
        g.setCupo(rs.getInt("cupo"));
        g.setHorario(rs.getString("horario") != null ? rs.getString("horario").trim() : null);

        g.setActividad(rs.getString("actividad"));
        g.setUsuario(rs.getString("usuario"));
        Timestamp ts = rs.getTimestamp("fechahora");
        g.setFechaHora(ts != null ? ts.toLocalDateTime() : null);

        return g;
    }
}