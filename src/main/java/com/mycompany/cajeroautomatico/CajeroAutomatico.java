package com.mycompany.cajeroautomatico;

import com.mycompany.cajeroautomatico.vista.CajeroAutomaticoView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal del sistema de Cajero Automático
 */
public class CajeroAutomatico {
    
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            CajeroAutomaticoView vista = new CajeroAutomaticoView();
            vista.setVisible(true);
        });
    }
}