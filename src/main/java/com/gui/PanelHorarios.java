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
    private JLabel lblNumeroGrupo;
    private JComboBox<String> cmbDia;   // [dia]
    private JSpinner spnCupo;

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

        // Listeners para los botones
// Listeners para los botones
        btnGuardar.addActionListener(e -> guardarHorario());
        btnEliminar.addActionListener(e -> eliminarHorarioSeleccionado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarHorarios());
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
        panelFormulario.add(new JLabel("Período:"), gbc);

        cmbCodigoPeriodo = new JComboBox<>(new String[]{"-- Seleccionar --"});
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        panelFormulario.add(cmbCodigoPeriodo, gbc);

        // 2. Asignatura (codigo_asignatura)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Asignatura:"), gbc);

        cmbCodigoAsignatura = new JComboBox<>(new String[]{"-- Seleccionar --"});
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(cmbCodigoAsignatura, gbc);

        // 3. Número de Grupo (numero_grupo)
// 3. Numero de Grupo (automatico, no editable)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Numero Grupo:"), gbc);

        lblNumeroGrupo = new JLabel("-");
        lblNumeroGrupo.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(lblNumeroGrupo, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Cupo:"), gbc);

        spnCupo = new JSpinner(new SpinnerNumberModel(30, 1, 100, 1));
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2;
        panelFormulario.add(spnCupo, gbc);

        // 4. Día de la Semana (dia)
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Día:"), gbc);

        cmbDia = new JComboBox<>(new String[]{"-- Seleccionar --", "1 - Lunes", "2 - Martes", "3 - Miércoles", "4 - Jueves", "5 - Viernes", "6 - Sábado"});
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(cmbDia, gbc);

        // 5. Hora Inicio (JSpinner con SpinnerDateModel)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Hora Inicio:"), gbc);

        spnHoraInicio = crearSpinnerHora(7);
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spnHoraInicio, "HH:mm");
        spnHoraInicio.setEditor(editorInicio);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormulario.add(spnHoraInicio, gbc);

        // 6. Hora Fin (JSpinner con SpinnerDateModel)
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Hora Fin:"), gbc);

        spnHoraFin = crearSpinnerHora(8);
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(spnHoraFin, "HH:mm");
        spnHoraFin.setEditor(editorFin);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panelFormulario.add(spnHoraFin, gbc);

        // ==========================================
        // PANEL BOTONES
        // ==========================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnGuardar = new JButton("Crear Horario");
        btnEliminar = new JButton("Eliminar Horario");
        btnLimpiar = new JButton("Limpiar");
        btnBuscar = new JButton("Buscar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnBuscar);

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

        tblHorarios.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
        cmbCodigoPeriodo.addActionListener(e -> actualizarNumeroAutomatico());
        cmbCodigoAsignatura.addActionListener(e -> actualizarNumeroAutomatico());
        add(panelIzquierdo, BorderLayout.WEST);
        add(scrollTabla, BorderLayout.CENTER);
    }

    // ==========================================
    // GETTERS
    // ==========================================
    public String getCmbCodigoPeriodo() { return cmbCodigoPeriodo.getSelectedItem() != null ? cmbCodigoPeriodo.getSelectedItem().toString() : ""; }
    public String getCmbCodigoAsignatura() { return cmbCodigoAsignatura.getSelectedItem() != null ? cmbCodigoAsignatura.getSelectedItem().toString() : ""; }
    public String getLblNumeroGrupo() { return lblNumeroGrupo.getText(); }
    public String getCmbDia() { return cmbDia.getSelectedItem() != null ? cmbDia.getSelectedItem().toString() : ""; }

    public JSpinner getSpnHoraInicio() { return spnHoraInicio; }
    public JSpinner getSpnHoraFin() { return spnHoraFin; }

    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JTable getTblHorarios() { return tblHorarios; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    /**
     * Limpia los campos del formulario.
     */
    public void limpiarFormulario() {
        if (cmbCodigoPeriodo.getItemCount() > 0) cmbCodigoPeriodo.setSelectedIndex(0);
        if (cmbCodigoAsignatura.getItemCount() > 0) cmbCodigoAsignatura.setSelectedIndex(0);
        if (cmbDia.getItemCount() > 0) cmbDia.setSelectedIndex(0);

        lblNumeroGrupo.setText("-");
        spnCupo.setValue(30);
    }

    /**
     * Guarda / Crea un nuevo horario usando los JSpinner de hora.
     */
    public void guardarHorario() {
        if (cmbCodigoPeriodo.getSelectedIndex() <= 0 ||
                cmbCodigoAsignatura.getSelectedIndex() <= 0 ||
                cmbDia.getSelectedIndex() <= 0) {

            JOptionPane.showMessageDialog(this,
                    "Por favor, complete Periodo, Asignatura y Dia.",
                    "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String periodo = cmbCodigoPeriodo.getSelectedItem().toString().trim();
            String asignaturaCombo = cmbCodigoAsignatura.getSelectedItem().toString();
            String asignatura = asignaturaCombo.split(" - ")[0].trim();
            String grupo = lblNumeroGrupo.getText().trim();
            int dia = cmbDia.getSelectedIndex();

            Date dateInicio = (Date) spnHoraInicio.getValue();
            LocalTime horaInicio = dateInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            Date dateFin = (Date) spnHoraFin.getValue();
            LocalTime horaFin = dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            if (!horaFin.isAfter(horaInicio)) {
                JOptionPane.showMessageDialog(this,
                        "La hora de fin debe ser posterior a la hora de inicio.",
                        "Hora Invalida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cupo = (Integer) spnCupo.getValue();
            Grupo nuevoGrupo = new Grupo(periodo, asignatura, grupo, cupo, "Pendiente");
            grupoDao.insertar(nuevoGrupo);

            HorarioGrupo nuevoHorario = new HorarioGrupo(periodo, asignatura, grupo, dia, horaInicio, horaFin);
            boolean guardado = horarioDao.insertar(nuevoHorario);

            if (guardado) {
                JOptionPane.showMessageDialog(this, "Grupo " + grupo + " creado con su horario.");
                buscarHorarios();
                actualizarNumeroAutomatico();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el horario.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error al procesar el horario: " + ex.getMessage());
        }
    }

    public void buscarHorarios() {
        if (cmbCodigoPeriodo.getSelectedIndex() <= 0 && cmbCodigoAsignatura.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un periodo o una asignatura para buscar.",
                    "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloTabla.setRowCount(0);

        List<HorarioGrupo> listaHorarios;

        if (cmbCodigoPeriodo.getSelectedIndex() <= 0) {
            listaHorarios = horarioDao.listarTodos();
        } else {
            String periodo = cmbCodigoPeriodo.getSelectedItem().toString().trim();
            listaHorarios = horarioDao.listarPorPeriodo(periodo);
        }

        if (cmbCodigoAsignatura.getSelectedIndex() > 0) {
            String asignaturaFiltro = cmbCodigoAsignatura.getSelectedItem().toString().split(" - ")[0].trim();
            listaHorarios.removeIf(h -> !h.getCodigoAsignatura().trim().equals(asignaturaFiltro));
        }

        List<DiaSemana> listaDias = diaSemanaDao.listarTodos();

        for (HorarioGrupo h : listaHorarios) {
            String nombreDia = "Dia " + h.getDia();
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


    public void eliminarHorarioSeleccionado() {
        int fila = tblHorarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un horario de la tabla para eliminar.",
                    "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Esta seguro de que desea eliminar el horario seleccionado?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String periodo = modeloTabla.getValueAt(fila, 0).toString();
            String asignatura = modeloTabla.getValueAt(fila, 1).toString();
            String grupo = modeloTabla.getValueAt(fila, 2).toString();
            String nombreDia = modeloTabla.getValueAt(fila, 3).toString();
            String horaInicioStr = modeloTabla.getValueAt(fila, 4).toString();

            int diaNumero = -1;
            List<DiaSemana> listaDias = diaSemanaDao.listarTodos();
            for (DiaSemana d : listaDias) {
                if (d.getDescripcion().trim().equalsIgnoreCase(nombreDia.trim())) {
                    diaNumero = d.getDia();
                    break;
                }
            }

            if (diaNumero == -1) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo identificar el dia '" + nombreDia + "' en la tabla Dia_Semana.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.time.LocalTime horaInicio = java.time.LocalTime.parse(horaInicioStr);

            boolean eliminado = horarioDao.eliminar(periodo, asignatura, grupo, diaNumero, horaInicio);

            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Horario eliminado correctamente.");
                buscarHorarios();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar. No se encontro ese horario exacto en la base de datos "
                                + "(periodo=" + periodo + ", asignatura=" + asignatura + ", grupo=" + grupo
                                + ", dia=" + diaNumero + ", hora=" + horaInicio + "). "
                                + "Puede que ya haya sido eliminado por otra sesion.",
                        "No se pudo eliminar", JOptionPane.WARNING_MESSAGE);
            }

        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo interpretar la hora del horario seleccionado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrio un error inesperado al eliminar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private JSpinner crearSpinnerHora(int horaInicial) {
        java.util.Calendar calMin = java.util.Calendar.getInstance();
        calMin.set(java.util.Calendar.HOUR_OF_DAY, 7);
        calMin.set(java.util.Calendar.MINUTE, 0);
        calMin.set(java.util.Calendar.SECOND, 0);
        calMin.set(java.util.Calendar.MILLISECOND, 0);
        Date horaMinima = calMin.getTime();

        java.util.Calendar calMax = java.util.Calendar.getInstance();
        calMax.set(java.util.Calendar.HOUR_OF_DAY, 22);
        calMax.set(java.util.Calendar.MINUTE, 0);
        calMax.set(java.util.Calendar.SECOND, 0);
        calMax.set(java.util.Calendar.MILLISECOND, 0);
        Date horaMaxima = calMax.getTime();

        java.util.Calendar calValor = java.util.Calendar.getInstance();
        calValor.set(java.util.Calendar.HOUR_OF_DAY, horaInicial);
        calValor.set(java.util.Calendar.MINUTE, 0);
        calValor.set(java.util.Calendar.SECOND, 0);
        calValor.set(java.util.Calendar.MILLISECOND, 0);
        Date valorInicial = calValor.getTime();

        SpinnerDateModel modelo = new SpinnerDateModel(valorInicial, horaMinima, horaMaxima, java.util.Calendar.HOUR_OF_DAY);
        return new JSpinner(modelo);
    }

    private void actualizarNumeroAutomatico() {
        if (cmbCodigoPeriodo.getSelectedIndex() <= 0 || cmbCodigoAsignatura.getSelectedIndex() <= 0) {
            lblNumeroGrupo.setText("-");
            return;
        }

        String periodo = cmbCodigoPeriodo.getSelectedItem().toString().trim();
        String asignatura = cmbCodigoAsignatura.getSelectedItem().toString().split(" - ")[0].trim();

        String siguiente = grupoDao.obtenerSiguienteNumero(periodo, asignatura);
        lblNumeroGrupo.setText(siguiente);
    }

    private void cargarSeleccion() {
        int fila = tblHorarios.getSelectedRow();
        if (fila == -1) return;

        String periodo = modeloTabla.getValueAt(fila, 0).toString();
        String asignatura = modeloTabla.getValueAt(fila, 1).toString();
        String grupo = modeloTabla.getValueAt(fila, 2).toString();

        for (int i = 0; i < cmbCodigoPeriodo.getItemCount(); i++) {
            if (cmbCodigoPeriodo.getItemAt(i).equals(periodo)) {
                cmbCodigoPeriodo.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cmbCodigoAsignatura.getItemCount(); i++) {
            String item = cmbCodigoAsignatura.getItemAt(i);
            if (item.startsWith(asignatura + " - ")) {
                cmbCodigoAsignatura.setSelectedIndex(i);
                break;
            }
        }

        lblNumeroGrupo.setText(grupo);
    }

    public void recargarCombos() {
        cargarComboPeriodos();
        cargarComboAsignaturas();
    }

}