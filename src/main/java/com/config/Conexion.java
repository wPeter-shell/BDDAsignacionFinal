package com.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:sqlserver://127.0.0.1:1433;databaseName=ProyectoFinal;encrypt=false;trustServerCertificate=true;";
    private static final String USUARIO = "admin";
    private static final String CLAVE = "12345";

    public static Connection conectar() {
        try {
            Connection con = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexion exitosa");
            return con;
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }

    }
}