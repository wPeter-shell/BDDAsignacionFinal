package com.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public MainFrame() {
        // 1. Configuración básica de la ventana
        setTitle("Sistema de Gestión Académica - Universidad");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 2. Cargar la interfaz
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelContenedor = new JPanel(new BorderLayout(10, 10));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Encabezado
        JPanel panelHeader = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("SISTEMA ACADÉMICO REGISTRO Y PREMATRÍCULA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(30, 60, 90));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        // Crear el panel de pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 12));

        // ---------------------------------------------------------------------
        // LAS 5 PESTAÑAS DEL SISTEMA
        // ---------------------------------------------------------------------

        // Pestaña 1: Mantenimiento de Estudiantes
        tabbedPane.addTab("1. Estudiantes",
                new PanelEstudiantes());

        // Pestaña 2: Mantenimiento de Asignaturas
        tabbedPane.addTab("2. Asignaturas",
                new PanelAsignaturas());

        // Pestaña 3: Horarios de Grupos (Punto 2 - Trigger)
        tabbedPane.addTab("3. Horarios de Grupos",
                new PanelHorarios());

        // Pestaña 4: Proceso de Inscripción / Prematricula (Punto 3)
        tabbedPane.addTab("4. Inscripción / Prematricula",
                 new PanelInscripcion());

        // Pestaña 5: Matriz de Horario Semanal y Créditos (Puntos 4 y 5)
        tabbedPane.addTab("5. Matriz y Reportes",
                new PanelReportes());

        // Agregar al contenedor
        panelContenedor.add(panelHeader, BorderLayout.NORTH);
        panelContenedor.add(tabbedPane, BorderLayout.CENTER);

        add(panelContenedor);
    }
}