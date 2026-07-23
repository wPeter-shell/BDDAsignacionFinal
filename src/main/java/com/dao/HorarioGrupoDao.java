package com.dao;

import com.model.HorarioGrupo;
import com.config.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HorarioGrupoDao {

    public List<HorarioGrupo> listarPorGrupo(String codigoPeriodo, String codigoAsignatura, String numeroGrupo) {
        List<HorarioGrupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Horario_Grupo WHERE codigo_periodo = ? AND codigo_asignatura = ? AND numero_grupo = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);
            ps.setString(2, codigoAsignatura);
            ps.setString(3, numeroGrupo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HorarioGrupo h = new HorarioGrupo();
                    h.setCodigoPeriodo(rs.getString("codigo_periodo").trim());
                    h.setCodigoAsignatura(rs.getString("codigo_asignatura").trim());
                    h.setNumeroGrupo(rs.getString("numero_grupo").trim());
                    h.setDia(rs.getInt("dia"));
                    h.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    h.setHoraFin(rs.getTime("hora_fin").toLocalTime());

                    h.setActividad(rs.getString("actividad"));
                    h.setUsuario(rs.getString("usuario"));
                    Timestamp ts = rs.getTimestamp("fechahora");
                    h.setFechaHora(ts != null ? ts.toLocalDateTime() : null);

                    lista.add(h);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar horarios del grupo: " + ex.getMessage());
        }

        return lista;
    }

    public List<HorarioGrupo> listarPorPeriodo(String codigoPeriodo) {
        List<HorarioGrupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Horario_Grupo WHERE codigo_periodo = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HorarioGrupo h = new HorarioGrupo();
                    h.setCodigoPeriodo(rs.getString("codigo_periodo").trim());
                    h.setCodigoAsignatura(rs.getString("codigo_asignatura").trim());
                    h.setNumeroGrupo(rs.getString("numero_grupo").trim());
                    h.setDia(rs.getInt("dia"));
                    h.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    h.setHoraFin(rs.getTime("hora_fin").toLocalTime());

                    h.setActividad(rs.getString("actividad"));
                    h.setUsuario(rs.getString("usuario"));
                    Timestamp ts = rs.getTimestamp("fechahora");
                    h.setFechaHora(ts != null ? ts.toLocalDateTime() : null);

                    lista.add(h);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar horarios del periodo: " + ex.getMessage());
        }

        return lista;
    }

    public boolean insertar(HorarioGrupo h) {
        String sql = "INSERT INTO Horario_Grupo (codigo_periodo, codigo_asignatura, numero_grupo, dia, hora_inicio, hora_fin) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, h.getCodigoPeriodo());
            ps.setString(2, h.getCodigoAsignatura());
            ps.setString(3, h.getNumeroGrupo());
            ps.setInt(4, h.getDia());
            ps.setTime(5, Time.valueOf(h.getHoraInicio()));
            ps.setTime(6, Time.valueOf(h.getHoraFin()));

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al insertar horario de grupo: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(String codigoPeriodo, String codigoAsignatura, String numeroGrupo, int dia, java.time.LocalTime horaInicio) {
        String sql = "DELETE FROM Horario_Grupo WHERE codigo_periodo = ? AND codigo_asignatura = ? AND numero_grupo = ? AND dia = ? AND hora_inicio = CAST(? AS TIME)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoPeriodo);
            ps.setString(2, codigoAsignatura);
            ps.setString(3, numeroGrupo);
            ps.setInt(4, dia);
            ps.setTime(5, Time.valueOf(horaInicio));

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error al eliminar horario de grupo: " + ex.getMessage());
            return false;
        }
    }

    public List<HorarioGrupo> listarTodos() {
        List<HorarioGrupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Horario_Grupo";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HorarioGrupo h = new HorarioGrupo();
                h.setCodigoPeriodo(rs.getString("codigo_periodo").trim());
                h.setCodigoAsignatura(rs.getString("codigo_asignatura").trim());
                h.setNumeroGrupo(rs.getString("numero_grupo").trim());
                h.setDia(rs.getInt("dia"));
                h.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                h.setHoraFin(rs.getTime("hora_fin").toLocalTime());

                h.setActividad(rs.getString("actividad"));
                h.setUsuario(rs.getString("usuario"));
                Timestamp ts = rs.getTimestamp("fechahora");
                h.setFechaHora(ts != null ? ts.toLocalDateTime() : null);

                lista.add(h);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar todos los horarios: " + ex.getMessage());
        }

        return lista;
    }
}