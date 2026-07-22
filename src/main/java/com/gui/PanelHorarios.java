package com.gui;

import com.dao.*;
import com.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Pestaña 3: Panel de gestión de Horarios de Grupos.
 * Mapeado exactamente con la tabla [Horario_Grupo] de la base de datos SQL Server.
 */
public class PanelHorarios extends JPanel {

    // --- Componentes de Selección (Combos y Spinners de Hora) ---
    private JComboBox<String> cmbCodigoPeriodo;   // [codigo_periodo]
    private JComboBox<String> cmbCodigoAsignatura;// [codigo_asignatura]
    private JComboBox<String> cmbNumeroGrupo;     // [numero_grupo]
    private JComboBox<String> cmbDia;             // [dia]

    // 🚀 Uso de JSpinner para selección adecuada de horas
    private JSpinner spnHoraInicio;               // [hora_inicio]
    private JSpinner spnHoraFin;                  // [hora_fin]

    // --- Botones de Acción ---
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    // --- Tabla y Modelo de Datos ---
    private JTable tblHorarios;
    private DefaultTableModel modeloTabla;

    private final HorarioGrupoDao horarioDao = new HorarioGrupoDao();
    private final DiaSemanaDao diaSemanaDao = new DiaSemanaDao();
    private final PeriodoAcademicoDao periodoDao = new PeriodoAcademicoDao();
    private final AsignaturaDao asignaturaDao = new AsignaturaDao();
    private final GrupoDao grupoDao = new GrupoDao();

    public PanelHorarios() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        cargarComboPeriodos();
        cargarComboAsignaturas();
        cargarComboGrupos();

        // Listeners para los botones
        btnGuardar.addActionListener(e -> guardarHorario());
        btnEliminar.addActionListener(e -> eliminarHorarioSeleccionado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
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

        cmbCodigoPeriodo = new JComboBox<>(new String[]{"-- Seleccionar --"});
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        panelFormulario.add(cmbCodigoPeriodo, gbc);

        // 2. Asignatura (codigo_asignatura)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Asignatura (codigo_asignatura):"), gbc);

        cmbCodigoAsignatura = new JComboBox<>(new String[]{"-- Seleccionar --"});
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(cmbCodigoAsignatura, gbc);

        // 3. Número de Grupo (numero_grupo)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Número Grupo (numero_grupo):"), gbc);

        cmbNumeroGrupo = new JComboBox<>(new String[]{"-- Seleccionar --"});
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(cmbNumeroGrupo, gbc);

        btnBuscar = new JButton("🔍 Filtrar");
        gbc.gridx = 2; gbc.gridy = 2;
        panelFormulario.add(btnBuscar, gbc);

        // 4. Día de la Semana (dia)
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Día (dia):"), gbc);

        cmbDia = new JComboBox<>(new String[]{"-- Seleccionar --", "1 - Lunes", "2 - Martes", "3 - Miércoles", "4 - Jueves", "5 - Viernes", "6 - Sábado", "7 - Domingo"});
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(cmbDia, gbc);

        // 5. Hora Inicio (JSpinner con SpinnerDateModel)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Hora Inicio:"), gbc);

        spnHoraInicio = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spnHoraInicio, "HH:mm");
        spnHoraInicio.setEditor(editorInicio);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormulario.add(spnHoraInicio, gbc);

        // 6. Hora Fin (JSpinner con SpinnerDateModel)
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Hora Fin:"), gbc);

        spnHoraFin = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(spnHoraFin, "HH:mm");
        spnHoraFin.setEditor(editorFin);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panelFormulario.add(spnHoraFin, gbc);

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

        cmbCodigoPeriodo.addActionListener(e -> cargarTablaPorPeriodo());
        add(panelIzquierdo, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
    }

    // ==========================================
    // GETTERS
    // ==========================================
    public String getCmbCodigoPeriodo() { return cmbCodigoPeriodo.getSelectedItem() != null ? cmbCodigoPeriodo.getSelectedItem().toString() : ""; }
    public String getCmbCodigoAsignatura() { return cmbCodigoAsignatura.getSelectedItem() != null ? cmbCodigoAsignatura.getSelectedItem().toString() : ""; }
    public String getCmbNumeroGrupo() { return cmbNumeroGrupo.getSelectedItem() != null ? cmbNumeroGrupo.getSelectedItem().toString() : ""; }
    public String getCmbDia() { return cmbDia.getSelectedItem() != null ? cmbDia.getSelectedItem().toString() : ""; }

    public JSpinner getSpnHoraInicio() { return spnHoraInicio; }
    public JSpinner getSpnHoraFin() { return spnHoraFin; }

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
        if (cmbCodigoPeriodo.getItemCount() > 0) cmbCodigoPeriodo.setSelectedIndex(0);
        if (cmbCodigoAsignatura.getItemCount() > 0) cmbCodigoAsignatura.setSelectedIndex(0);
        if (cmbNumeroGrupo.getItemCount() > 0) cmbNumeroGrupo.setSelectedIndex(0);
        if (cmbDia.getItemCount() > 0) cmbDia.setSelectedIndex(0);

        spnHoraInicio.setValue(new Date());
        spnHoraFin.setValue(new Date());
    }

    /**
     * Guarda / Crea un nuevo horario usando los JSpinner de hora.
     */
    public void guardarHorario() {
        if (cmbCodigoPeriodo.getSelectedIndex() <= 0 ||
                cmbCodigoAsignatura.getSelectedIndex() <= 0 ||
                cmbNumeroGrupo.getSelectedIndex() <= 0 ||
                cmbDia.getSelectedIndex() <= 0) {

            JOptionPane.showMessageDialog(this,
                    "⚠️ Por favor, complete todos los campos de selección (Período, Asignatura, Grupo y Día).",
                    "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String periodo = cmbCodigoPeriodo.getSelectedItem().toString().trim();

            String asignaturaCombo = cmbCodigoAsignatura.getSelectedItem().toString();
            String asignatura = asignaturaCombo.split(" - ")[0].trim();

            String grupo = cmbNumeroGrupo.getSelectedItem().toString().trim();

            // Extraer el número de día del combo (Ej: "1 - Lunes" -> 1)
            int dia = cmbDia.getSelectedIndex();

            // Convertir las horas de los JSpinner a LocalTime
            Date dateInicio = (Date) spnHoraInicio.getValue();
            LocalTime horaInicio = dateInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            Date dateFin = (Date) spnHoraFin.getValue();
            LocalTime horaFin = dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            // Validar que la hora fin sea posterior a la de inicio
            if (!horaFin.isAfter(horaInicio)) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ La hora de fin debe ser posterior a la hora de inicio.",
                        "Hora Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear objeto modelo
            HorarioGrupo nuevoHorario = new HorarioGrupo(periodo, asignatura, grupo, dia, horaInicio, horaFin);

            // Guardar con tu DAO
            boolean guardado = horarioDao.insertar(nuevoHorario);

            if (guardado) {
                JOptionPane.showMessageDialog(this, "✅ Horario guardado correctamente.");
                cargarTablaPorPeriodo();
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo guardar el horario en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Ocurrió un error al procesar el horario: " + ex.getMessage());
        }
    }

    public void cargarTablaPorPeriodo() {
        modeloTabla.setRowCount(0);

        if (cmbCodigoPeriodo.getSelectedIndex() <= 0) {
            return;
        }

        String periodo = cmbCodigoPeriodo.getSelectedItem().toString();

        List<DiaSemana> listaDias = diaSemanaDao.listarTodos();
        List<HorarioGrupo> listaHorarios = horarioDao.listarPorPeriodo(periodo);

        for (HorarioGrupo h : listaHorarios) {
            String nombreDia = "Día " + h.getDia();
            for (DiaSemana d : listaDias) {
                if (d.getDia() == h.getDia()) {
                    nombreDia = d.getDescripcion();
                    break;
                }
            }

            modeloTabla.addRow(new Object[]{
                    h.getCodigoPeriodo(),
                    h.getCodigoAsignatura(),
                    h.getNumeroGrupo(),
                    nombreDia,
                    h.getHoraInicio(),
                    h.getHoraFin()
            });
        }
    }

    public void cargarComboPeriodos() {
        cmbCodigoPeriodo.removeAllItems();
        cmbCodigoPeriodo.addItem("-- Seleccionar --");

        List<PeriodoAcademico> lista = periodoDao.listarTodos();
        for (PeriodoAcademico p : lista) {
            cmbCodigoPeriodo.addItem(p.getCodigo());
        }
    }

    public void cargarComboAsignaturas() {
        cmbCodigoAsignatura.removeAllItems();
        cmbCodigoAsignatura.addItem("-- Seleccionar --");

        List<Asignatura> lista = asignaturaDao.listarTodos();
        for (Asignatura a : lista) {
            cmbCodigoAsignatura.addItem(a.getCodigo() + " - " + a.getNombre());
        }
    }

    public void cargarComboGrupos() {
        cmbNumeroGrupo.removeAllItems();
        cmbNumeroGrupo.addItem("-- Seleccionar --");

        List<Grupo> lista = grupoDao.listarTodos();

        for (Grupo g : lista) {
            String numero = g.getNumero();

            boolean existe = false;
            for (int i = 0; i < cmbNumeroGrupo.getItemCount(); i++) {
                if (cmbNumeroGrupo.getItemAt(i).equals(numero)) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                cmbNumeroGrupo.addItem(numero);
            }
        }
    }

    public void eliminarHorarioSeleccionado() {
        int fila = tblHorarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Por favor, seleccione un horario de la tabla para eliminar.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar el horario seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        String periodo = modeloTabla.getValueAt(fila, 0).toString();
        String asignatura = modeloTabla.getValueAt(fila, 1).toString();
        String grupo = modeloTabla.getValueAt(fila, 2).toString();
        String nombreDia = modeloTabla.getValueAt(fila, 3).toString();
        String horaInicioStr = modeloTabla.getValueAt(fila, 4).toString();

        int diaNumero = 1;
        List<DiaSemana> listaDias = diaSemanaDao.listarTodos();
        for (DiaSemana d : listaDias) {
            if (d.getDescripcion().equalsIgnoreCase(nombreDia)) {
                diaNumero = d.getDia();
                break;
            }
        }

        java.time.LocalTime horaInicio = java.time.LocalTime.parse(horaInicioStr);

        boolean eliminado = horarioDao.eliminar(periodo, asignatura, grupo, diaNumero, horaInicio);

        if (eliminado) {
            JOptionPane.showMessageDialog(this, "✅ Horario eliminado correctamente.");
            cargarTablaPorPeriodo();
        } else {
            JOptionPane.showMessageDialog(this, "❌ No se pudo eliminar el registro de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}