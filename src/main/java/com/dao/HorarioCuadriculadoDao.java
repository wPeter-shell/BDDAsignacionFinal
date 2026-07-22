package com.dao;

import com.config.Conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HorarioCuadriculadoDao {

    private static final String[] COLUMNAS_MATRIZ = {"Hora", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

    public List<Object[]> obtenerMatriz(String idEstudiante, String codigoPeriodo) {
        List<Object[]> filas = new ArrayList<>();
        String sql = "{call HorarioCuadriculado(?, ?)}";

        try (Connection con = Conexion.conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, idEstudiante);
            cs.setString(2, codigoPeriodo);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[7];
                    for (int i = 0; i < 7; i++) {
                        Object valor = rs.getObject(COLUMNAS_MATRIZ[i]);
                        fila[i] = valor != null ? valor.toString() : "";
                    }
                    filas.add(fila);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al obtener matriz de horario: " + ex.getMessage());
        }

        return filas;
    }

    public List<Object[]> obtenerDetalleInscripcion(String idEstudiante, String codigoPeriodo) {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT ASG.codigo, GI.numero_grupo, ASG.nombre, ASG.creditos, G.horario "
                + "FROM Grupo_Inscrito GI "
                + "INNER JOIN Asignatura ASG ON GI.codigo_asignatura = ASG.codigo "
                + "INNER JOIN Grupo G ON G.codigo_periodo = GI.codigo_periodo "
                + "    AND G.codigo_asignatura = GI.codigo_asignatura "
                + "    AND G.numero = GI.numero_grupo "
                + "WHERE GI.id_estudiante = ? AND GI.codigo_periodo = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idEstudiante);
            ps.setString(2, codigoPeriodo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[]{
                            rs.getString("codigo").trim(),
                            rs.getString("numero_grupo").trim(),
                            rs.getString("nombre").trim(),
                            rs.getInt("creditos"),
                            rs.getString("horario") != null ? rs.getString("horario").trim() : ""
                    });
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al obtener detalle de inscripcion: " + ex.getMessage());
        }

        return filas;
    }
}