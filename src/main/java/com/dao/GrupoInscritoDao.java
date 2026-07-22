package com.dao;

import Logic.GrupoInscrito;
import Conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GrupoInscritoDao {

    public List<GrupoInscrito> listarPorEstudiante(String codigoPeriodo, String idEstudiante) {
        List<GrupoInscrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM Grupo_Inscrito WHERE codigo_periodo = ? AND id_estudiante = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);
            ps.setString(2, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar grupos inscritos: " + ex.getMessage());
        }

        return lista;
    }

    public List<GrupoInscrito> listarTodos() {
        List<GrupoInscrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM Grupo_Inscrito";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar grupos inscritos: " + ex.getMessage());
        }

        return lista;
    }

    public boolean insertar(GrupoInscrito gi) {
        String sql = "INSERT INTO Grupo_Inscrito (codigo_periodo, id_estudiante, codigo_asignatura, numero_grupo) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, gi.getCodigoPeriodo());
            ps.setString(2, gi.getIdEstudiante());
            ps.setString(3, gi.getCodigoAsignatura());
            ps.setString(4, gi.getNumeroGrupo());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al insertar grupo inscrito: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(String codigoPeriodo, String idEstudiante, String codigoAsignatura, String numeroGrupo) {
        String sql = "DELETE FROM Grupo_Inscrito WHERE codigo_periodo = ? AND id_estudiante = ? AND codigo_asignatura = ? AND numero_grupo = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);
            ps.setString(2, idEstudiante);
            ps.setString(3, codigoAsignatura);
            ps.setString(4, numeroGrupo);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al eliminar grupo inscrito: " + ex.getMessage());
            return false;
        }
    }

    private GrupoInscrito mapear(ResultSet rs) throws SQLException {
        GrupoInscrito gi = new GrupoInscrito();
        gi.setCodigoPeriodo(rs.getString("codigo_periodo").trim());
        gi.setIdEstudiante(rs.getString("id_estudiante").trim());
        gi.setCodigoAsignatura(rs.getString("codigo_asignatura").trim());
        gi.setNumeroGrupo(rs.getString("numero_grupo").trim());
        gi.setActividad(rs.getString("actividad"));
        gi.setUsuario(rs.getString("usuario"));
        Timestamp ts = rs.getTimestamp("fechahora");
        gi.setFechaHora(ts != null ? ts.toLocalDateTime() : null);
        return gi;
    }
}