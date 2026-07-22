package com.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pestaña 3: Panel de gestión de Horarios de Grupos.
 * Mapeado exactamente con la tabla [Horario_Grupo] de la base de datos SQL Server.
 */
public class PanelHorarios extends JPanel {

    // --- Componentes de Selección (Combos y Spinners) ---
    private JComboBox<String> cmbCodigoPeriodo;   // [codigo_periodo]
    private JComboBox<String> cmbCodigoAsignatura;// [codigo_asignatura]
    private JSpinner spnNumeroGrupo;              // [numero_grupo]
    private JComboBox<String> cmbDia;             // [dia]
    private JTextField txtHoraInicio;             // [hora_inicio] (Ej: 08:00)
    private JTextField txtHoraFin;                // [hora_fin]    (Ej: 10:00)

    // --- Botones de Acción ---
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    // --- Tabla y Modelo de Datos ---
    private JTable tblHorarios;
    private DefaultTableModel modeloTabla;

    public PanelHorarios() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // ==========================================
        // PANEL FORMULARIO (Datos del Horario)
        // ==========================================
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(" Asignación de Horario a Grupo "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Período Académico (codigo_periodo)
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Período (codigo_periodo):"), gbc);

        cmbCodigoPeriodo = new JComboBox<>(new String[]{"-- Seleccionar --", "2026-1", "2026-2"});
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        panelFormulario.add(cmbCodigoPeriodo, gbc);

        // 2. Asignatura (codigo_asignatura)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Asignatura (codigo_asignatura):"), gbc);

        cmbCodigoAsignatura = new JComboBox<>(new String[]{"-- Seleccionar --", "ICC-100T - Prog. I", "MAT-101 - Cálculo"});
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(cmbCodigoAsignatura, gbc);

        // 3. Número de Grupo (numero_grupo)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Número Grupo (numero_grupo):"), gbc);

        spnNumeroGrupo = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(spnNumeroGrupo, gbc);

        btnBuscar = new JButton("🔍 Filtrar");
        gbc.gridx = 2; gbc.gridy = 2;
        panelFormulario.add(btnBuscar, gbc);

        // 4. Día de la Semana (dia)
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Día (dia):"), gbc);

        cmbDia = new JComboBox<>(new String[]{"-- Seleccionar --", "L - Lunes", "M - Martes", "MI - Miércoles", "J - Jueves", "V - Viernes", "S - Sábado"});
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(cmbDia, gbc);

        // 5. Hora Inicio (hora_inicio)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Hora Inicio (hora_inicio):"), gbc);

        txtHoraInicio = new JTextField("08:00");
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormulario.add(txtHoraInicio, gbc);

        // 6. Hora Fin (hora_fin)
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Hora Fin (hora_fin):"), gbc);

        txtHoraFin = new JTextField("10:00");
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panelFormulario.add(txtHoraFin, gbc);

        // ==========================================
        // PANEL BOTONES
        // ==========================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnGuardar = new JButton("💾 Crear Horario");
        btnEliminar = new JButton("🗑️ Eliminar Horario");
        btnLimpiar = new JButton("🧹 Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        // ==========================================
        // TABLA DE HORARIOS (Lista de Horarios)
        // ==========================================
        String[] columnas = {"codigo_periodo", "codigo_asignatura", "numero_grupo", "dia", "hora_inicio", "hora_fin"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHorarios = new JTable(modeloTabla);
        tblHorarios.setRowHeight(22);
        JScrollPane scrollTabla = new JScrollPane(tblHorarios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(" Lista de Horarios Asignados "));

        add(panelIzquierdo, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
    }

    // ==========================================
    // GETTERS (Alineados con la Lógica DAO)
    // ==========================================
    public String getCmbCodigoPeriodo() { return cmbCodigoPeriodo.getSelectedItem().toString(); }
    public String getCmbCodigoAsignatura() { return cmbCodigoAsignatura.getSelectedItem().toString(); }
    public int getSpnNumeroGrupo() { return (Integer) spnNumeroGrupo.getValue(); }
    public String getCmbDia() { return cmbDia.getSelectedItem().toString(); }
    public String getTxtHoraInicio() { return txtHoraInicio.getText().trim(); }
    public String getTxtHoraFin() { return txtHoraFin.getText().trim(); }

    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JTable getTblHorarios() { return tblHorarios; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    /**
     * Limpia los campos del formulario.
     */
    public void limpiarFormulario() {
        cmbCodigoPeriodo.setSelectedIndex(0);
        cmbCodigoAsignatura.setSelectedIndex(0);
        spnNumeroGrupo.setValue(1);
        cmbDia.setSelectedIndex(0);
        txtHoraInicio.setText("08:00");
        txtHoraFin.setText("10:00");
    }
}