package com.gui;

import com.dao.*;
import com.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Pestaña 4: Panel de Inscripción / Prematricula de Materias.
 * Utiliza Selección de Estudiante por JComboBox y Doble Tabla (Disponibles vs Inscritas).
 */
public class PanelInscripcion extends JPanel {

    // --- Componentes de Selección ---
    private JComboBox<String> cmbEstudiante;     // [id_estudiante] - Lista de estudiantes
    private JComboBox<String> cmbCodigoPeriodo;  // [codigo_periodo] - Lista de períodos

    // --- Tablas y Modelos ---
    private JTable tblMateriasDisponibles;       // Materias ofertadas en el período
    private DefaultTableModel modeloDisponibles;

    private JTable tblMateriasInscritas;         // Materias inscritas por el estudiante
    private DefaultTableModel modeloInscritas;

    // --- Botones Centrales ---
    private JButton btnAgregarMateria;
    private JButton btnRetirarMateria;

    // --- DAOs ---
    private final EstudianteDao estudianteDao = new EstudianteDao();
    private final PeriodoAcademicoDao periodoDao = new PeriodoAcademicoDao();
    private final GrupoDao grupoDao = new GrupoDao();
    private final GrupoInscritoDao grupoInscritoDao = new GrupoInscritoDao();
    private final InscripcionDao inscripcionDao = new InscripcionDao();

    public PanelInscripcion() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        cargarCombos();

        // --- Listeners de Selección Automática ---
        cmbEstudiante.addActionListener(e -> cargarTablas());
        cmbCodigoPeriodo.addActionListener(e -> cargarTablas());

        // --- Listeners de Botones ---
        btnAgregarMateria.addActionListener(e -> inscribirGrupo());
        btnRetirarMateria.addActionListener(e -> retirarGrupo());
    }

    private void inicializarComponentes() {
        // ==========================================
        // 1. PANEL SUPERIOR: Filtros con ComboBoxes
        // ==========================================
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder(" Selección de Estudiante y Período "));

        panelFiltros.add(new JLabel("Estudiante:"));
        cmbEstudiante = new JComboBox<>(new String[]{"-- Seleccionar --"});
        cmbEstudiante.setPreferredSize(new Dimension(300, 25));
        panelFiltros.add(cmbEstudiante);

        panelFiltros.add(new JLabel("Período:"));
        cmbCodigoPeriodo = new JComboBox<>(new String[]{"-- Seleccionar --"});
        panelFiltros.add(cmbCodigoPeriodo);

        // ==========================================
        // 2. TABLAS (Disponibles vs Inscritas)
        // ==========================================
        String[] columnas = {"Asignatura", "Grupo", "Cupo", "Horario General"};

        modeloDisponibles = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblMateriasDisponibles = new JTable(modeloDisponibles);
        JScrollPane scrollDisponibles = new JScrollPane(tblMateriasDisponibles);
        scrollDisponibles.setBorder(BorderFactory.createTitledBorder(" Grupos Disponibles "));

        modeloInscritas = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblMateriasInscritas = new JTable(modeloInscritas);
        JScrollPane scrollInscritas = new JScrollPane(tblMateriasInscritas);
        scrollInscritas.setBorder(BorderFactory.createTitledBorder(" Grupos Inscritos "));

        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 10, 0));
        panelTablas.add(scrollDisponibles);
        panelTablas.add(scrollInscritas);

        // ==========================================
        // 3. BOTONES CENTRALES
        // ==========================================
        JPanel panelBotonesCentrales = new JPanel();
        panelBotonesCentrales.setLayout(new BoxLayout(panelBotonesCentrales, BoxLayout.Y_AXIS));

        btnAgregarMateria = new JButton("➡️ Inscribir");
        btnRetirarMateria = new JButton("⬅️ Retirar");

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
    // MÉTODOS DE LÓGICA Y CARGA DE DATOS
    // ==========================================

    public void cargarCombos() {
        // Cargar Estudiantes
        cmbEstudiante.removeAllItems();
        cmbEstudiante.addItem("-- Seleccionar --");
        for (Estudiante e : estudianteDao.listarTodos()) {
            cmbEstudiante.addItem(e.getId() + " - " + e.getNombre() + " " + e.getApellio());
        }

        // Cargar Períodos Académicos
        cmbCodigoPeriodo.removeAllItems();
        cmbCodigoPeriodo.addItem("-- Seleccionar --");
        for (PeriodoAcademico p : periodoDao.listarTodos()) {
            cmbCodigoPeriodo.addItem(p.getCodigo());
        }
    }

    private String obtenerIdEstudiante() {
        if (cmbEstudiante.getSelectedIndex() <= 0) return null;
        String seleccion = cmbEstudiante.getSelectedItem().toString();
        return seleccion.split(" - ")[0].trim();
    }

    private String obtenerPeriodo() {
        if (cmbCodigoPeriodo.getSelectedIndex() <= 0) return null;
        return cmbCodigoPeriodo.getSelectedItem().toString().trim();
    }

    private void cargarTablas() {
        modeloDisponibles.setRowCount(0);
        modeloInscritas.setRowCount(0);

        String idEstudiante = obtenerIdEstudiante();
        String periodo = obtenerPeriodo();

        if (idEstudiante == null || periodo == null) {
            return;
        }

        // 1. Grupos inscritos por el estudiante
        List<GrupoInscrito> listaInscritos = grupoInscritoDao.listarPorEstudiante(periodo, idEstudiante);

        // 2. Todos los grupos ofertados
        List<Grupo> todosLosGrupos = grupoDao.listarTodos();

        // Llenar TABLA DERECHA: Grupos Inscritos
        for (GrupoInscrito gi : listaInscritos) {
            String horarioGeneral = "-";
            String cupo = "-";

            for (Grupo g : todosLosGrupos) {
                if (g.getCodigoPeriodo().equalsIgnoreCase(gi.getCodigoPeriodo()) &&
                        g.getCodigoAsignatura().equalsIgnoreCase(gi.getCodigoAsignatura()) &&
                        g.getNumero().equalsIgnoreCase(gi.getNumeroGrupo())) {
                    horarioGeneral = g.getHorario();
                    cupo = String.valueOf(g.getCupo());
                    break;
                }
            }

            modeloInscritas.addRow(new Object[]{
                    gi.getCodigoAsignatura(),
                    gi.getNumeroGrupo(),
                    cupo,
                    horarioGeneral
            });
        }

        // Llenar TABLA IZQUIERDA: Grupos Disponibles (del período y que no estén inscritos)
        for (Grupo g : todosLosGrupos) {
            if (g.getCodigoPeriodo().equalsIgnoreCase(periodo)) {

                boolean yaInscrito = false;
                for (GrupoInscrito gi : listaInscritos) {
                    if (gi.getCodigoAsignatura().equalsIgnoreCase(g.getCodigoAsignatura()) &&
                            gi.getNumeroGrupo().equalsIgnoreCase(g.getNumero())) {
                        yaInscrito = true;
                        break;
                    }
                }

                if (!yaInscrito) {
                    modeloDisponibles.addRow(new Object[]{
                            g.getCodigoAsignatura(),
                            g.getNumero(),
                            g.getCupo(),
                            g.getHorario()
                    });
                }
            }
        }
    }

    private void inscribirGrupo() {
        int fila = tblMateriasDisponibles.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un grupo disponible de la tabla izquierda.");
            return;
        }

        String idEstudiante = obtenerIdEstudiante();
        String periodo = obtenerPeriodo();

        if (idEstudiante == null || periodo == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Debe seleccionar un Estudiante y un Período.");
            return;
        }

        String asignatura = modeloDisponibles.getValueAt(fila, 0).toString();
        String grupo = modeloDisponibles.getValueAt(fila, 1).toString();

        // Asegurar registro maestro en Inscripcion si aún no existe
        if (!inscripcionDao.existe(periodo, idEstudiante)) {
            Inscripcion nuevaInscripcion = new Inscripcion(periodo, idEstudiante, LocalDate.now());
            inscripcionDao.insertar(nuevaInscripcion);
        }

        // Insertar en Grupo_Inscrito
        GrupoInscrito nuevoGrupoInscrito = new GrupoInscrito(periodo, idEstudiante, asignatura, grupo);
        boolean insertado = grupoInscritoDao.insertar(nuevoGrupoInscrito);

        if (insertado) {
            JOptionPane.showMessageDialog(this, "✅ Materia inscrita exitosamente.");
            cargarTablas();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al inscribir la materia.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retirarGrupo() {
        int fila = tblMateriasInscritas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione una materia inscrita de la tabla derecha para retirar.");
            return;
        }

        String idEstudiante = obtenerIdEstudiante();
        String periodo = obtenerPeriodo();

        if (idEstudiante == null || periodo == null) return;

        String asignatura = modeloInscritas.getValueAt(fila, 0).toString();
        String grupo = modeloInscritas.getValueAt(fila, 1).toString();

        boolean eliminado = grupoInscritoDao.eliminar(periodo, idEstudiante, asignatura, grupo);

        if (eliminado) {
            JOptionPane.showMessageDialog(this, "✅ Materia retirada correctamente.");
            cargarTablas();
        } else {
            JOptionPane.showMessageDialog(this, "❌ No se pudo retirar la materia.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================
    // GETTERS
    // ==========================================
    public JComboBox<String> getCmbEstudiante() { return cmbEstudiante; }
    public JComboBox<String> getCmbCodigoPeriodo() { return cmbCodigoPeriodo; }
    public JButton getBtnAgregarMateria() { return btnAgregarMateria; }
    public JButton getBtnRetirarMateria() { return btnRetirarMateria; }
    public JTable getTblMateriasDisponibles() { return tblMateriasDisponibles; }
    public DefaultTableModel getModeloDisponibles() { return modeloDisponibles; }
    public JTable getTblMateriasInscritas() { return tblMateriasInscritas; }
    public DefaultTableModel getModeloInscritas() { return modeloInscritas; }
}