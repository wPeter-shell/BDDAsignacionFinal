package com.gui;

import com.dao.EstudianteDao;
import com.dao.HorarioCuadriculadoDao;
import com.dao.PeriodoAcademicoDao;
import com.model.Estudiante;
import com.model.PeriodoAcademico;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pestana 5: Informe Unificado de Inscripcion y Horario Semanal.
 * Muestra toda la informacion de la prematricula en una sola vista continua.
 */
public class PanelReportes extends JPanel {

    private final EstudianteDao estudianteDao = new EstudianteDao();
    private final PeriodoAcademicoDao periodoAcademicoDao = new PeriodoAcademicoDao();
    private final HorarioCuadriculadoDao horarioDao = new HorarioCuadriculadoDao();

    private JComboBox<Estudiante> cmbEstudiante;
    private JComboBox<PeriodoAcademico> cmbCodigoPeriodo;
    private JButton btnGenerarReporte;

    private JLabel lblPeriodoVal;
    private JLabel lblEstudianteVal;
    private JLabel lblCarreraVal;

    private JTable tblInformeInscritos;
    private DefaultTableModel modeloInforme;

    private JTable tblMatrizHorario;
    private DefaultTableModel modeloMatriz;

    private JLabel lblTotalGrupos;
    private JLabel lblTotalCreditos;

    public PanelReportes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        cargarPeriodos();
        cargarEstudiantes();
        conectarEventos();
    }

    private void inicializarComponentes() {
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder(" Criterios de Seleccion "));

        panelFiltros.add(new JLabel("Estudiante:"));
        cmbEstudiante = new JComboBox<>();
        cmbEstudiante.setPreferredSize(new Dimension(240, cmbEstudiante.getPreferredSize().height));
        cmbEstudiante.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Estudiante) {
                    Estudiante e = (Estudiante) value;
                    setText(e.getId() + " - " + e.getNombre() + " " + e.getApellio());
                }
                return this;
            }
        });
        panelFiltros.add(cmbEstudiante);

        panelFiltros.add(new JLabel("Periodo Academico:"));
        cmbCodigoPeriodo = new JComboBox<>();
        panelFiltros.add(cmbCodigoPeriodo);

        btnGenerarReporte = new JButton("Generar Informe Completo");
        btnGenerarReporte.setFont(new Font("Arial", Font.BOLD, 12));
        btnGenerarReporte.setBackground(new Color(220, 235, 252));
        panelFiltros.add(btnGenerarReporte);

        add(panelFiltros, BorderLayout.NORTH);

        JPanel panelDocumento = new JPanel();
        panelDocumento.setLayout(new BoxLayout(panelDocumento, BoxLayout.Y_AXIS));
        panelDocumento.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDocumento.setBackground(Color.WHITE);

        JPanel panelCabecera = new JPanel(new GridLayout(2, 2, 10, 8));
        panelCabecera.setBorder(BorderFactory.createTitledBorder(" 1. Datos Generales de la Prematricula "));
        panelCabecera.setOpaque(false);

        lblEstudianteVal = new JLabel("Estudiante: [Sin Seleccionar]");
        lblEstudianteVal.setFont(new Font("Arial", Font.BOLD, 13));
        lblEstudianteVal.setForeground(new Color(20, 70, 150));

        lblPeriodoVal = new JLabel("Periodo Academico: [Sin Seleccionar]");
        lblPeriodoVal.setFont(new Font("Arial", Font.BOLD, 12));

        lblCarreraVal = new JLabel("Carrera: -");
        lblCarreraVal.setFont(new Font("Arial", Font.PLAIN, 12));

        panelCabecera.add(lblEstudianteVal);
        panelCabecera.add(lblPeriodoVal);
        panelCabecera.add(lblCarreraVal);

        String[] colInforme = {"Codigo", "Grupo", "Nombre Asignatura", "Creditos", "Horario Condensado"};
        modeloInforme = new DefaultTableModel(colInforme, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblInformeInscritos = new JTable(modeloInforme);
        tblInformeInscritos.setRowHeight(22);

        JScrollPane scrollInforme = new JScrollPane(tblInformeInscritos);
        scrollInforme.setBorder(BorderFactory.createTitledBorder(" 2. Detalle de Asignaturas Inscritas "));
        scrollInforme.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollInforme.setPreferredSize(new Dimension(800, 150));
        scrollInforme.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel panelTotales = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 5));
        panelTotales.setOpaque(false);

        lblTotalGrupos = new JLabel("Total Grupos Inscritos: 0");
        lblTotalGrupos.setFont(new Font("Arial", Font.BOLD, 12));

        lblTotalCreditos = new JLabel("Total Creditos Academicos: 0");
        lblTotalCreditos.setFont(new Font("Arial", Font.BOLD, 13));
        lblTotalCreditos.setForeground(new Color(0, 120, 50));

        panelTotales.add(lblTotalGrupos);
        panelTotales.add(lblTotalCreditos);

        String[] colMatriz = {"Hora", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};
        modeloMatriz = new DefaultTableModel(colMatriz, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMatrizHorario = new JTable(modeloMatriz);
        tblMatrizHorario.setRowHeight(28);
        tblMatrizHorario.setGridColor(new Color(220, 220, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblMatrizHorario.getColumnCount(); i++) {
            tblMatrizHorario.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollMatriz = new JScrollPane(tblMatrizHorario);
        scrollMatriz.setBorder(BorderFactory.createTitledBorder(" 3. Matriz de Horario Semanal (Grid) "));
        scrollMatriz.setPreferredSize(new Dimension(800, 480));

        panelDocumento.add(panelCabecera);
        panelDocumento.add(Box.createVerticalStrut(10));
        panelDocumento.add(scrollInforme);
        panelDocumento.add(panelTotales);
        panelDocumento.add(Box.createVerticalStrut(10));
        panelDocumento.add(scrollMatriz);

        JScrollPane scrollGeneral = new JScrollPane(panelDocumento);
        scrollGeneral.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollGeneral, BorderLayout.CENTER);
    }

    private void cargarEstudiantes() {
        cmbEstudiante.removeAllItems();
        for (Estudiante e : estudianteDao.listarTodos()) {
            cmbEstudiante.addItem(e);
        }
    }

    private void cargarPeriodos() {
        cmbCodigoPeriodo.removeAllItems();
        for (PeriodoAcademico p : periodoAcademicoDao.listarTodos()) {
            cmbCodigoPeriodo.addItem(p);
        }
    }

    public void recargarCombos() {
        cargarPeriodos();
        cargarEstudiantes();
    }

    private void conectarEventos() {
        btnGenerarReporte.addActionListener(e -> generarInforme());
    }

    private void generarInforme() {
        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        PeriodoAcademico periodo = (PeriodoAcademico) cmbCodigoPeriodo.getSelectedItem();

        if (estudiante == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante.");
            return;
        }
        if (periodo == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un periodo academico.");
            return;
        }

        String idEstudiante = estudiante.getId();
        String nombreCompleto = formatearNombreCompleto(estudiante);
        lblEstudianteVal.setText("Estudiante: " + estudiante.getId() + " - " + nombreCompleto);
        lblPeriodoVal.setText("Periodo Academico: " + periodo.getDescripcion());
        lblCarreraVal.setText("Carrera: " + estudiante.getIdCarrera());

        cargarDetalleInscripcion(idEstudiante, periodo.getCodigo());
        cargarMatrizHorario(idEstudiante, periodo.getCodigo());
    }

    private void cargarDetalleInscripcion(String idEstudiante, String codigoPeriodo) {
        modeloInforme.setRowCount(0);
        List<Object[]> filas = horarioDao.obtenerDetalleInscripcion(idEstudiante, codigoPeriodo);

        int totalCreditos = 0;
        for (Object[] fila : filas) {
            modeloInforme.addRow(fila);
            totalCreditos += (Integer) fila[3];
        }

        lblTotalGrupos.setText("Total Grupos Inscritos: " + filas.size());
        lblTotalCreditos.setText("Total Creditos Academicos: " + totalCreditos);
    }

    private void cargarMatrizHorario(String idEstudiante, String codigoPeriodo) {
        modeloMatriz.setRowCount(0);
        List<Object[]> filas = horarioDao.obtenerMatriz(idEstudiante, codigoPeriodo);
        for (Object[] fila : filas) {
            modeloMatriz.addRow(fila);
        }
    }

    private String formatearNombreCompleto(Estudiante e) {
        String nombreFormateado = abreviarPalabrasExtra(e.getNombre());
        String apellidoFormateado = abreviarPalabrasExtra(e.getApellio());
        return nombreFormateado + " " + apellidoFormateado;
    }

    private String abreviarPalabrasExtra(String textoCompleto) {
        if (textoCompleto == null || textoCompleto.trim().isEmpty()) {
            return "";
        }

        String[] palabras = textoCompleto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder(palabras[0]);

        for (int i = 1; i < palabras.length; i++) {
            resultado.append(" ").append(palabras[i].charAt(0)).append(".");
        }

        return resultado.toString();
    }
}