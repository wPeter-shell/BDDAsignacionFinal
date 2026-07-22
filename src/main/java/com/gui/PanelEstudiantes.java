package com.gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pestaña 1: Panel de gestión para el CRUD de Estudiantes.
 * Alineado con la tabla [Estudiante] de la base de datos SQL Server.
 */
public class PanelEstudiantes extends JPanel {

    // --- Componentes del Formulario (Inputs) ---
    private JTextField txtId;                 // [id]
    private JTextField txtNombre;             // [nombre]
    private JTextField txtApellido;           // [apellio]
    private JComboBox<String> cmbIdCarrera;   // [id_carrera]
    private JComboBox<String> cmbIdCategoriaPago; // [id_categoria_pago]
    private JComboBox<String> cmbIdNacionalidad;  // [id_nacionalidad]
    private JTextField txtDireccion;          // [direccion]

    // --- Botones de Acción ---
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    // --- Tabla y Modelo de Datos ---
    private JTable tblEstudiantes;
    private DefaultTableModel modeloTabla;

    public PanelEstudiantes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Inicializar la interfaz visual
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // ==========================================
        // PANELS DE FORMULARIO (Norte / Izquierda)
        // ==========================================
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(" Datos del Estudiante "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Matrícula / ID
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Matrícula (ID):"), gbc);

        txtId = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 0;
        panelFormulario.add(txtId, gbc);

        btnBuscar = new JButton("🔍 Buscar");
        gbc.gridx = 2; gbc.gridy = 0;
        panelFormulario.add(btnBuscar, gbc);

        // 2. Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre:"), gbc);

        txtNombre = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtNombre, gbc);

        // 3. Apellido
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Apellido:"), gbc);

        txtApellido = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(txtApellido, gbc);

        // 4. Carrera (id_carrera)
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Carrera:"), gbc);

        cmbIdCarrera = new JComboBox<>(new String[]{"-- Seleccionar --", "INSI - Ing. Sistemas", "DERE - Derecho", "MEDI - Medicina"});
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(cmbIdCarrera, gbc);

        // 5. Categoría Pago (id_categoria_pago)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Categoría Pago:"), gbc);

        cmbIdCategoriaPago = new JComboBox<>(new String[]{"-- Seleccionar --", "REG - Regular", "BEC - Becado", "CON - Convenio"});
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormulario.add(cmbIdCategoriaPago, gbc);

        // 6. Nacionalidad (id_nacionalidad)
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Nacionalidad:"), gbc);

        cmbIdNacionalidad = new JComboBox<>(new String[]{"-- Seleccionar --", "DOM - Dominicana", "COL - Colombiana", "USA - Estadounidense"});
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panelFormulario.add(cmbIdNacionalidad, gbc);

        // 7. Dirección
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Dirección:"), gbc);

        txtDireccion = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2;
        panelFormulario.add(txtDireccion, gbc);

        // ==========================================
        // PANEL DE BOTONES (CRUD)
        // ==========================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnGuardar = new JButton("💾 Guardar");
        btnActualizar = new JButton("✏️ Actualizar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnLimpiar = new JButton("🧹 Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Panel Izquierdo que agrupa Formulario + Botones
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        // ==========================================
        // TABLA DE ESTUDIANTES (Centro / Derecha)
        // ==========================================
        String[] columnas = {"ID", "Nombre", "Apellido", "Carrera", "Pago", "Nacionalidad", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactivar edición directa en la celda
            }
        };

        tblEstudiantes = new JTable(modeloTabla);
        tblEstudiantes.setRowHeight(22);
        JScrollPane scrollTabla = new JScrollPane(tblEstudiantes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(" Listado de Estudiantes Registrados "));

        // Agregar paneles principales al PanelEstudiantes
        add(panelIzquierdo, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
    }

    // ==========================================
    // MÉTODOS GETTERS (Para conectar con la BD)
    // ==========================================
    public String getTxtId() { return txtId.getText().trim(); }
    public String getTxtNombre() { return txtNombre.getText().trim(); }
    public String getTxtApellido() { return txtApellido.getText().trim(); }
    public String getCmbIdCarrera() { return cmbIdCarrera.getSelectedItem().toString(); }
    public String getCmbIdCategoriaPago() { return cmbIdCategoriaPago.getSelectedItem().toString(); }
    public String getCmbIdNacionalidad() { return cmbIdNacionalidad.getSelectedItem().toString(); }
    public String getTxtDireccion() { return txtDireccion.getText().trim(); }

    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JTable getTblEstudiantes() { return tblEstudiantes; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    /**
     * Método helper para limpiar los campos del formulario.
     */
    public void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        cmbIdCarrera.setSelectedIndex(0);
        cmbIdCategoriaPago.setSelectedIndex(0);
        cmbIdNacionalidad.setSelectedIndex(0);
        txtDireccion.setText("");
        txtId.requestFocus();
    }
}