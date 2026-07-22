package com.main;

import com.gui.MainFrame;
import javax.swing.*;

/**
 * Punto de entrada principal de la aplicación académica.
 */
public class MainApp {

    public static void main(String[] args) {
        // Configurar el aspecto visual (Look and Feel) acorde al Sistema Operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo aplicar el Look and Feel del sistema: " + e.getMessage());
        }

        // Iniciar la interfaz gráfica en el hilo de eventos de Swing (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            MainFrame ventanaPrincipal = new MainFrame();
            ventanaPrincipal.setVisible(true);
        });
    }
}