package com.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pestaña 2: Panel de gestión para el CRUD de Asignaturas.
 * Mapeado exactamente con la tabla [Asignatura] incluyendo Horas Teóricas y Prácticas.
 */
public class PanelAsignaturas extends JPanel {

    // --- Componentes del Formulario (Inputs Mapeados) ---
    private JTextField txtCodigo;           // Column: [codigo]
    private JTextField txtNombre;           // Column: [nombre]
    private JSpinner spnCreditos;           // Column: [creditos]
    private JSpinner spnHorasTeoricas;      // Column: [horas_teoricas]
    private JSpinner spnHorasPracticas;     // Column: [horas_practicas]

    // --- Botones de Acción ---
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    // --- Tabla y Modelo de Datos ---
    private JTable tblAsignaturas;
    private DefaultTableModel modeloTabla;

    public PanelAsignaturas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // ==========================================
        // PANEL FORMULARIO (Datos de la Asignatura)
        // ==========================================
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(" Datos de la Asignatura "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Código (codigo)
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Código (codigo):"), gbc);

        txtCodigo = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 0;
        panelFormulario.add(txtCodigo, gbc);

        btnBuscar = new JButton("🔍 Buscar");
        gbc.gridx = 2; gbc.gridy = 0;
        panelFormulario.add(btnBuscar, gbc);

        // 2. Nombre (nombre)
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre (nombre):"), gbc);

        txtNombre = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtNombre, gbc);

        // 3. Créditos (creditos)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Créditos (creditos):"), gbc);

        spnCreditos = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(spnCreditos, gbc);

        // 4. Horas Teóricas (horas_teoricas)
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Horas Teóricas (horas_teoricas):"), gbc);

        spnHorasTeoricas = new JSpinner(new SpinnerNumberModel(2, 0, 10, 1));
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(spnHorasTeoricas, gbc);

        // 5. Horas Prácticas (horas_practicas)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Horas Prácticas (horas_practicas):"), gbc);

        spnHorasPracticas = new JSpinner(new SpinnerNumberModel(2, 0, 10, 1));
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormulario.add(spnHorasPracticas, gbc);

        // ==========================================
        // PANEL DE BOTONES (CRUD)
        // ==========================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnGuardar = new JButton("💾 Guardar");
        btnActualizar = new JButton("✏️ Actualizar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnLimpiar = new JButton("🧹 Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Agrupar formulario y botones a la izquierda
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        // ==========================================
        // TABLA DE ASIGNATURAS (Centro / Derecha)
        // ==========================================
        String[] columnas = {"codigo", "nombre", "creditos", "horas_teoricas", "horas_practicas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactivar edición directa en las celdas
            }
        };

        tblAsignaturas = new JTable(modeloTabla);
        tblAsignaturas.setRowHeight(22);
        JScrollPane scrollTabla = new JScrollPane(tblAsignaturas);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(" Listado de Asignaturas "));

        add(panelIzquierdo, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
    }

    // ==========================================
    // GETTERS (Alineados con la Lógica DAO)
    // ==========================================
    public String getTxtCodigo() { return txtCodigo.getText().trim(); }
    public String getTxtNombre() { return txtNombre.getText().trim(); }
    public int getSpnCreditos() { return (Integer) spnCreditos.getValue(); }
    public int getSpnHorasTeoricas() { return (Integer) spnHorasTeoricas.getValue(); }
    public int getSpnHorasPracticas() { return (Integer) spnHorasPracticas.getValue(); }

    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JTable getTblAsignaturas() { return tblAsignaturas; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    /**
     * Limpia los campos del formulario.
     */
    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        spnCreditos.setValue(3);
        spnHorasTeoricas.setValue(2);
        spnHorasPracticas.setValue(2);
        txtCodigo.requestFocus();
    }
}