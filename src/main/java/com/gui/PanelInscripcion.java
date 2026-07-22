package com.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pestaña 4: Panel de Inscripción / Prematricula de Materias.
 * Utiliza un diseño de Doble Tabla (Disponibles vs Inscritas).
 */
public class PanelInscripcion extends JPanel {

    // --- Componentes de Búsqueda y Filtro ---
    private JTextField txtIdEstudiante;          // [id_estudiante]
    private JComboBox<String> cmbCodigoPeriodo;  // [codigo_periodo]
    private JButton btnBuscarEstudiante;
    private JLabel lblNombreEstudiante;

    // --- Tablas y Modelos ---
    private JTable tblMateriasDisponibles;       // Materias ofertadas
    private DefaultTableModel modeloDisponibles;

    private JTable tblMateriasInscritas;         // Materias inscritas
    private DefaultTableModel modeloInscritas;

    // --- Botones Centrales ---
    private JButton btnAgregarMateria;
    private JButton btnRetirarMateria;

    public PanelInscripcion() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // ==========================================
        // 1. PANEL SUPERIOR: Filtros con UX Mejorado
        // ==========================================
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder(" Inscripción de Estudiante "));

        // Matrícula
        panelFiltros.add(new JLabel("Matrícula:"));
        txtIdEstudiante = new JTextField(10);
        panelFiltros.add(txtIdEstudiante);

        // Período
        panelFiltros.add(new JLabel("Período:"));
        cmbCodigoPeriodo = new JComboBox<>(new String[]{"-- Seleccionar --", "2026-1", "2026-2"});
        panelFiltros.add(cmbCodigoPeriodo);

        // Botón Buscar (Ubicado justo al lado del Período para ejecutar la búsqueda)
        btnBuscarEstudiante = new JButton("🔍 Buscar");
        panelFiltros.add(btnBuscarEstudiante);

        // Etiqueta de resultado del Estudiante
        lblNombreEstudiante = new JLabel("Estudiante: [Sin Seleccionar]");
        lblNombreEstudiante.setFont(new Font("Arial", Font.BOLD, 12));
        lblNombreEstudiante.setForeground(new Color(30, 80, 150));
        panelFiltros.add(Box.createHorizontalStrut(15)); // Espaciador visual
        panelFiltros.add(lblNombreEstudiante);

        // ==========================================
        // 2. PANEL CENTRAL: Las Dos Tablas y Botones
        // ==========================================
        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 10, 0));

        // --- TABLA IZQUIERDA: Materias Disponibles ---
        String[] colDisponibles = {"Código", "Asignatura", "Grupo", "Créditos"};
        modeloDisponibles = new DefaultTableModel(colDisponibles, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMateriasDisponibles = new JTable(modeloDisponibles);
        tblMateriasDisponibles.setRowHeight(22);
        JScrollPane scrollDisponibles = new JScrollPane(tblMateriasDisponibles);
        scrollDisponibles.setBorder(BorderFactory.createTitledBorder(" Materias Disponibles "));

        // --- TABLA DERECHA: Materias Inscritas ---
        String[] colInscritas = {"Código", "Asignatura", "Grupo", "Créditos"};
        modeloInscritas = new DefaultTableModel(colInscritas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMateriasInscritas = new JTable(modeloInscritas);
        tblMateriasInscritas.setRowHeight(22);
        JScrollPane scrollInscritas = new JScrollPane(tblMateriasInscritas);
        scrollInscritas.setBorder(BorderFactory.createTitledBorder(" Materias Inscritas "));

        panelTablas.add(scrollDisponibles);
        panelTablas.add(scrollInscritas);

        // --- BOTONES CENTRALES DE ACCIÓN ---
        JPanel panelBotonesCentrales = new JPanel();
        panelBotonesCentrales.setLayout(new BoxLayout(panelBotonesCentrales, BoxLayout.Y_AXIS));
        panelBotonesCentrales.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));

        btnAgregarMateria = new JButton("Inscribir ➡️");
        btnAgregarMateria.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRetirarMateria = new JButton("⬅️ Retirar");
        btnRetirarMateria.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelBotonesCentrales.add(Box.createVerticalGlue());
        panelBotonesCentrales.add(btnAgregarMateria);
        panelBotonesCentrales.add(Box.createVerticalStrut(15));
        panelBotonesCentrales.add(btnRetirarMateria);
        panelBotonesCentrales.add(Box.createVerticalGlue());

        JPanel panelCentroFinal = new JPanel(new BorderLayout(5, 0));
        panelCentroFinal.add(panelTablas, BorderLayout.CENTER);
        panelCentroFinal.add(panelBotonesCentrales, BorderLayout.EAST);

        // Armar el contenedor principal
        add(panelFiltros, BorderLayout.NORTH);
        add(panelCentroFinal, BorderLayout.CENTER);
    }

    // ==========================================
    // GETTERS
    // ==========================================
    public String getTxtIdEstudiante() { return txtIdEstudiante.getText().trim(); }
    public String getCmbCodigoPeriodo() { return cmbCodigoPeriodo.getSelectedItem().toString(); }

    public JButton getBtnBuscarEstudiante() { return btnBuscarEstudiante; }
    public JButton getBtnAgregarMateria() { return btnAgregarMateria; }
    public JButton getBtnRetirarMateria() { return btnRetirarMateria; }

    public JLabel getLblNombreEstudiante() { return lblNombreEstudiante; }

    public JTable getTblMateriasDisponibles() { return tblMateriasDisponibles; }
    public DefaultTableModel getModeloDisponibles() { return modeloDisponibles; }

    public JTable getTblMateriasInscritas() { return tblMateriasInscritas; }
    public DefaultTableModel getModeloInscritas() { return modeloInscritas; }
}