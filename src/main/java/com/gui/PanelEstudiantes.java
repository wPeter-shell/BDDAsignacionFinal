package com.gui;

import com.dao.CarreraDao;
import com.dao.CategoriaPagoDao;
import com.dao.EstudianteDao;
import com.dao.NacionalidadDao;
import com.model.Carrera;
import com.model.CategoriaPago;
import com.model.Estudiante;
import com.model.Nacionalidad;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelEstudiantes extends JPanel {

    private final EstudianteDao estudianteDao = new EstudianteDao();
    private final CarreraDao carreraDao = new CarreraDao();
    private final CategoriaPagoDao categoriaPagoDao = new CategoriaPagoDao();
    private final NacionalidadDao nacionalidadDao = new NacionalidadDao();

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JComboBox<Carrera> cmbIdCarrera;
    private JComboBox<CategoriaPago> cmbIdCategoriaPago;
    private JComboBox<Nacionalidad> cmbIdNacionalidad;
    private JTextField txtDireccion;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    private JTable tblEstudiantes;
    private DefaultTableModel modeloTabla;

    public PanelEstudiantes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        cargarCombos();
        cargarTabla();
        conectarEventos();
    }

    private void inicializarComponentes() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(" Datos del Estudiante "));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Matricula (ID):"), gbc);
        txtId = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 0;
        panelFormulario.add(txtId, gbc);
        btnBuscar = new JButton("Buscar");
        gbc.gridx = 2; gbc.gridy = 0;
        panelFormulario.add(btnBuscar, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Apellido:"), gbc);
        txtApellido = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(txtApellido, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Carrera:"), gbc);
        cmbIdCarrera = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(cmbIdCarrera, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Categoria Pago:"), gbc);
        cmbIdCategoriaPago = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormulario.add(cmbIdCategoriaPago, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Nacionalidad:"), gbc);
        cmbIdNacionalidad = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panelFormulario.add(cmbIdNacionalidad, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Direccion:"), gbc);
        txtDireccion = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2;
        panelFormulario.add(txtDireccion, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        String[] columnas = {"ID", "Nombre", "Apellido", "Carrera", "Pago", "Nacionalidad", "Direccion"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblEstudiantes = new JTable(modeloTabla);
        tblEstudiantes.setRowHeight(22);
        JScrollPane scrollTabla = new JScrollPane(tblEstudiantes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(" Listado de Estudiantes Registrados "));

        add(panelIzquierdo, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void cargarCombos() {
        cmbIdCarrera.removeAllItems();
        for (Carrera c : carreraDao.listarTodos()) {
            cmbIdCarrera.addItem(c);
        }

        cmbIdCategoriaPago.removeAllItems();
        for (CategoriaPago c : categoriaPagoDao.listarTodos()) {
            cmbIdCategoriaPago.addItem(c);
        }

        cmbIdNacionalidad.removeAllItems();
        for (Nacionalidad n : nacionalidadDao.listarTodos()) {
            cmbIdNacionalidad.addItem(n);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Estudiante> lista = estudianteDao.listarTodos();
        for (Estudiante e : lista) {
            modeloTabla.addRow(new Object[]{
                    e.getId(), e.getNombre(), e.getApellio(),
                    e.getIdCarrera(), e.getIdCategoriaPago(), e.getIdNacionalidad(),
                    e.getDireccion()
            });
        }
    }

    private void buscarPorId() {
        String textoBusqueda = txtId.getText().trim();

        if (textoBusqueda.isEmpty()) {
            cargarTabla();
            return;
        }

        modeloTabla.setRowCount(0);
        List<Estudiante> lista = estudianteDao.listarTodos();
        for (Estudiante e : lista) {
            if (e.getId().trim().contains(textoBusqueda)) {
                modeloTabla.addRow(new Object[]{
                        e.getId(), e.getNombre(), e.getApellio(),
                        e.getIdCarrera(), e.getIdCategoriaPago(), e.getIdNacionalidad(),
                        e.getDireccion()
                });
            }
        }
    }

    private void conectarEventos() {
        tblEstudiantes.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarPorId());
    }

    private void cargarSeleccion() {
        int fila = tblEstudiantes.getSelectedRow();
        if (fila == -1) return;

        txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtApellido.setText(modeloTabla.getValueAt(fila, 2).toString());
        seleccionarComboPorId(cmbIdCarrera, modeloTabla.getValueAt(fila, 3).toString());
        seleccionarComboPorId(cmbIdCategoriaPago, modeloTabla.getValueAt(fila, 4).toString());
        seleccionarComboPorId(cmbIdNacionalidad, modeloTabla.getValueAt(fila, 5).toString());
        Object direccion = modeloTabla.getValueAt(fila, 6);
        txtDireccion.setText(direccion != null ? direccion.toString() : "");
        txtId.setEditable(false);
    }

    private void seleccionarComboPorId(JComboBox<?> combo, String id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            String itemId = null;

            if (item instanceof Carrera) itemId = ((Carrera) item).getId();
            if (item instanceof CategoriaPago) itemId = ((CategoriaPago) item).getId();
            if (item instanceof Nacionalidad) itemId = ((Nacionalidad) item).getId();

            if (itemId != null && itemId.trim().equals(id.trim())) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private Estudiante construirEstudianteDesdeFormulario() {
        Estudiante e = new Estudiante();
        e.setId(txtId.getText().trim());
        e.setNombre(txtNombre.getText().trim());
        e.setApellio(txtApellido.getText().trim());
        e.setDireccion(txtDireccion.getText().trim());

        Carrera carrera = (Carrera) cmbIdCarrera.getSelectedItem();
        CategoriaPago categoriaPago = (CategoriaPago) cmbIdCategoriaPago.getSelectedItem();
        Nacionalidad nacionalidad = (Nacionalidad) cmbIdNacionalidad.getSelectedItem();

        e.setIdCarrera(carrera != null ? carrera.getId() : null);
        e.setIdCategoriaPago(categoriaPago != null ? categoriaPago.getId() : null);
        e.setIdNacionalidad(nacionalidad != null ? nacionalidad.getId() : null);

        return e;
    }

    private void guardar() {
        String nuevoId = estudianteDao.obtenerSiguienteId();

        if (nuevoId == null) {
            JOptionPane.showMessageDialog(this, "No se pudo generar el Id automaticamente.");
            return;
        }

        Estudiante e = construirEstudianteDesdeFormulario();
        e.setId(nuevoId);

        boolean exito = estudianteDao.insertar(e);
        JOptionPane.showMessageDialog(this, exito
                ? "Estudiante guardado con matricula: " + nuevoId
                : "No se pudo guardar.");

        if (exito) {
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void actualizar() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante de la tabla antes de actualizar.");
            return;
        }

        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacio.");
            return;
        }
        if (txtApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El apellido no puede estar vacio.");
            return;
        }
        if (cmbIdCarrera.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una carrera.");
            return;
        }
        if (cmbIdCategoriaPago.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoria de pago.");
            return;
        }
        if (cmbIdNacionalidad.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una nacionalidad.");
            return;
        }
        if (txtDireccion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La direccion no puede estar vacia.");
            return;
        }

        boolean exito = estudianteDao.actualizar(construirEstudianteDesdeFormulario());
        JOptionPane.showMessageDialog(this, exito ? "Estudiante actualizado." : "No se pudo actualizar.");
        if (exito) {
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void eliminar() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante de la tabla.");
            return;
        }

        String id = txtId.getText().trim();
        List<String> dependencias = estudianteDao.obtenerDependencias(id);

        if (!dependencias.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("No se puede eliminar este estudiante porque:\n\n");
            for (String razon : dependencias) {
                mensaje.append("- ").append(razon).append("\n");
            }
            mensaje.append("\nElimina primero esos registros relacionados.");
            JOptionPane.showMessageDialog(this, mensaje.toString(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "Seguro que deseas eliminar este estudiante?");
        if (confirmacion != JOptionPane.YES_OPTION) return;

        boolean exito = estudianteDao.eliminar(id);
        JOptionPane.showMessageDialog(this, exito ? "Estudiante eliminado." : "No se pudo eliminar.");
        if (exito) {
            cargarTabla();
            limpiarFormulario();
        }
    }

    public void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDireccion.setText("");
        txtId.setEditable(true);
        txtId.setEditable(false);
        tblEstudiantes.clearSelection();
        cargarTabla();
    }
}