package com.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pestaña 5: Informe Unificado de Inscripción y Horario Semanal.
 * Muestra toda la información de la prematricula en una sola vista continua.
 */
public class PanelReportes extends JPanel {

    // --- Filtros de Búsqueda ---
    private JTextField txtIdEstudiante;          // [Estudiante.id]
    private JComboBox<String> cmbCodigoPeriodo;  // [Periodo_Academico.codigo]
    private JButton btnGenerarReporte;

    // --- Datos del Informe (Cabecera) ---
    private JLabel lblPeriodoVal;
    private JLabel lblEstudianteVal;            // Formato: "10203040 - Pedro A. Martínez P."
    private JLabel lblCarreraVal;               // Formato: "INSI - Ingeniería en Sistemas"

    // --- Tabla 1: Informe Detallado de Grupos Inscritos ---
    private JTable tblInformeInscritos;
    private DefaultTableModel modeloInforme;

    // --- Tabla 2: Matriz Semanal de Horario ---
    private JTable tblMatrizHorario;
    private DefaultTableModel modeloMatriz;

    // --- Pie del Informe ---
    private JLabel lblTotalGrupos;
    private JLabel lblTotalCreditos;

    public PanelReportes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // =========================================================================
        // 1. FILTROS SUPERIORES (Fuera del documento)
        // =========================================================================
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder(" Criterios de Selección "));

        panelFiltros.add(new JLabel("Matrícula / ID:"));
        txtIdEstudiante = new JTextField(10);
        panelFiltros.add(txtIdEstudiante);

        panelFiltros.add(new JLabel("Período Académico:"));
        cmbCodigoPeriodo = new JComboBox<>(new String[]{"-- Seleccionar --", "2026-1", "2026-2"});
        panelFiltros.add(cmbCodigoPeriodo);

        btnGenerarReporte = new JButton("📄 Generar Informe Completo");
        btnGenerarReporte.setFont(new Font("Arial", Font.BOLD, 12));
        btnGenerarReporte.setBackground(new Color(220, 235, 252));
        panelFiltros.add(btnGenerarReporte);

        add(panelFiltros, BorderLayout.NORTH);

        // =========================================================================
        // 2. CUERPO DEL INFORME (Vista Única e Integrada)
        // =========================================================================
        JPanel panelDocumento = new JPanel();
        panelDocumento.setLayout(new BoxLayout(panelDocumento, BoxLayout.Y_AXIS));
        panelDocumento.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDocumento.setBackground(Color.WHITE);

        // --- A. Encabezado del Informe ---
        JPanel panelCabecera = new JPanel(new GridLayout(2, 2, 10, 8));
        panelCabecera.setBorder(BorderFactory.createTitledBorder(" 1. Datos Generales de la Prematricula "));
        panelCabecera.setOpaque(false);

        lblEstudianteVal = new JLabel("Estudiante: [Sin Seleccionar]");
        lblEstudianteVal.setFont(new Font("Arial", Font.BOLD, 13));
        lblEstudianteVal.setForeground(new Color(20, 70, 150));

        lblPeriodoVal = new JLabel("Período Académico: [Sin Seleccionar]");
        lblPeriodoVal.setFont(new Font("Arial", Font.BOLD, 12));

        lblCarreraVal = new JLabel("Carrera: -");
        lblCarreraVal.setFont(new Font("Arial", Font.PLAIN, 12));

        panelCabecera.add(lblEstudianteVal);
        panelCabecera.add(lblPeriodoVal);
        panelCabecera.add(lblCarreraVal);

        // --- B. Tabla de Asignaturas Inscritas ---
        String[] colInforme = {"Código", "Grupo", "Nombre Asignatura", "Créditos", "Horario Condensado"};
        modeloInforme = new DefaultTableModel(colInforme, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblInformeInscritos = new JTable(modeloInforme);
        tblInformeInscritos.setRowHeight(22);

        JScrollPane scrollInforme = new JScrollPane(tblInformeInscritos);
        scrollInforme.setBorder(BorderFactory.createTitledBorder(" 2. Detalle de Asignaturas Inscritas "));
        scrollInforme.setPreferredSize(new Dimension(800, 150));

        // --- C. Resumen de Totales ---
        JPanel panelTotales = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 5));
        panelTotales.setOpaque(false);

        lblTotalGrupos = new JLabel("Total Grupos Inscritos: 0");
        lblTotalGrupos.setFont(new Font("Arial", Font.BOLD, 12));

        lblTotalCreditos = new JLabel("Total Créditos Académicos: 0");
        lblTotalCreditos.setFont(new Font("Arial", Font.BOLD, 13));
        lblTotalCreditos.setForeground(new Color(0, 120, 50));

        panelTotales.add(lblTotalGrupos);
        panelTotales.add(lblTotalCreditos);

        // --- D. Matriz Semanal de Horarios (Grid) ---
        String[] colMatriz = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
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
        scrollMatriz.setPreferredSize(new Dimension(800, 250));

        cargarHorariosBase(); // Carga las filas por defecto de 08:00 a 21:00

        // Ensamblar todo el documento en orden descendente
        panelDocumento.add(panelCabecera);
        panelDocumento.add(Box.createVerticalStrut(10));
        panelDocumento.add(scrollInforme);
        panelDocumento.add(panelTotales);
        panelDocumento.add(Box.createVerticalStrut(10));
        panelDocumento.add(scrollMatriz);

        // Poner todo dentro de un JScrollPane general por si la pantalla es pequeña
        JScrollPane scrollGeneral = new JScrollPane(panelDocumento);
        scrollGeneral.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollGeneral, BorderLayout.CENTER);
    }

    /**
     * Llena los bloques de horas de la matriz semanal.
     */
    private void cargarHorariosBase() {
        modeloMatriz.setRowCount(0);
        String[] bloques = {
                "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
                "12:00 - 13:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00",
                "17:00 - 18:00", "18:00 - 19:00", "19:00 - 20:00", "20:00 - 21:00"
        };
        for (String b : bloques) {
            modeloMatriz.addRow(new Object[]{b, "", "", "", "", "", ""});
        }
    }

    // ==========================================
    // GETTERS
    // ==========================================
    public String getTxtIdEstudiante() { return txtIdEstudiante.getText().trim(); }
    public String getCmbCodigoPeriodo() { return cmbCodigoPeriodo.getSelectedItem().toString(); }

    public JButton getBtnGenerarReporte() { return btnGenerarReporte; }

    public JLabel getLblPeriodoVal() { return lblPeriodoVal; }
    public JLabel getLblEstudianteVal() { return lblEstudianteVal; }
    public JLabel getLblCarreraVal() { return lblCarreraVal; }
    public JLabel getLblTotalGrupos() { return lblTotalGrupos; }
    public JLabel getLblTotalCreditos() { return lblTotalCreditos; }

    public JTable getTblInformeInscritos() { return tblInformeInscritos; }
    public DefaultTableModel getModeloInforme() { return modeloInforme; }

    public JTable getTblMatrizHorario() { return tblMatrizHorario; }
    public DefaultTableModel getModeloMatriz() { return modeloMatriz; }
}