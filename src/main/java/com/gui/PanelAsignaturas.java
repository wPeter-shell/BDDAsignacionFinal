package com.gui;

import com.dao.AsignaturaDao;
import com.model.Asignatura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Pestaña 2: Panel de gestión para el CRUD de Asignaturas.
 * Mapeado exactamente con la tabla [Asignatura] incluyendo Horas Teóricas y Prácticas.
 */
public class PanelAsignaturas extends JPanel {

    // --- Instancia del DAO ---
    private final AsignaturaDao asignaturaDao = new AsignaturaDao();

    // --- Componentes del Formulario ---
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
        asignarEventos(); // Conecta todos los botones y eventos de ratón
        cargarTabla();    // Carga los datos de la BD al abrir la pantalla
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

        // 1. Código
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Código (codigo):"), gbc);

        txtCodigo = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 0;
        panelFormulario.add(txtCodigo, gbc);

        btnBuscar = new JButton("🔍 Buscar");
        gbc.gridx = 2; gbc.gridy = 0;
        panelFormulario.add(btnBuscar, gbc);

        // 2. Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre (nombre):"), gbc);

        txtNombre = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtNombre, gbc);

        // 3. Créditos
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Créditos (creditos):"), gbc);

        spnCreditos = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(spnCreditos, gbc);

        // 4. Horas Teóricas
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Horas Teóricas (horas_teoricas):"), gbc);

        spnHorasTeoricas = new JSpinner(new SpinnerNumberModel(2, 0, 10, 1));
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(spnHorasTeoricas, gbc);

        // 5. Horas Prácticas
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
                return false;
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
    // REGISTRO DE EVENTOS (LISTENERS)
    // ==========================================
    private void asignarEventos() {
        btnGuardar.addActionListener(e -> guardarAsignatura());
        btnActualizar.addActionListener(e -> actualizarAsignatura());
        btnEliminar.addActionListener(e -> eliminarAsignatura());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Evento al hacer clic en una fila de la tabla para cargar los datos en los inputs
        tblAsignaturas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblAsignaturas.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosDesdeTabla(fila);
                }
            }
        });
    }

    // ==========================================
    // FUNCIONES CRUD
    // ==========================================
    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Asignatura> lista = asignaturaDao.listarTodos();
        for (Asignatura a : lista) {
            modeloTabla.addRow(new Object[]{
                    a.getCodigo(),
                    a.getNombre(),
                    a.getCreditos(),
                    a.getHorasTeoricas(),
                    a.getHorasPracticas()
            });
        }
    }

    private void cargarDatosDesdeTabla(int fila) {
        txtCodigo.setText(modeloTabla.getValueAt(fila, 0).toString());
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        spnCreditos.setValue(Integer.parseInt(modeloTabla.getValueAt(fila, 2).toString()));
        spnHorasTeoricas.setValue(Integer.parseInt(modeloTabla.getValueAt(fila, 3).toString()));
        spnHorasPracticas.setValue(Integer.parseInt(modeloTabla.getValueAt(fila, 4).toString()));
    }

    private void guardarAsignatura() {
        if (getTxtCodigo().isEmpty() || getTxtNombre().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El Código y el Nombre son obligatorios.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Asignatura a = new Asignatura(
                getTxtCodigo(),
                getTxtNombre(),
                getSpnCreditos(),
                getSpnHorasTeoricas(),
                getSpnHorasPracticas()
        );

        if (asignaturaDao.insertar(a)) {
            JOptionPane.showMessageDialog(this, "✅ Asignatura guardada correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar. Revisa si el código ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarAsignatura() {
        if (getTxtCodigo().isEmpty() || getTxtNombre().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un registro de la tabla o escriba un Código y Nombre válidos.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Asignatura a = new Asignatura(
                getTxtCodigo(),
                getTxtNombre(),
                getSpnCreditos(),
                getSpnHorasTeoricas(),
                getSpnHorasPracticas()
        );

        if (asignaturaDao.actualizar(a)) {
            JOptionPane.showMessageDialog(this, "✅ Asignatura actualizada con éxito.");
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al actualizar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarAsignatura() {
        int fila = tblAsignaturas.getSelectedRow();
        String codigo = "";

        if (fila >= 0) {
            codigo = modeloTabla.getValueAt(fila, 0).toString().trim();
        } else {
            codigo = txtCodigo.getText().trim();
        }

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Debe ingresar o seleccionar el código de la asignatura a eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la asignatura con código: " + codigo + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (asignaturaDao.eliminar(codigo)) {
                JOptionPane.showMessageDialog(this, "🗑️ Asignatura eliminada con éxito.");
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo eliminar la asignatura.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        spnCreditos.setValue(3);
        spnHorasTeoricas.setValue(2);
        spnHorasPracticas.setValue(2);
        tblAsignaturas.clearSelection();
        txtCodigo.requestFocus();
    }

    // ==========================================
    // GETTERS
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
}