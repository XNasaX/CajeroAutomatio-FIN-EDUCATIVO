package com.mycompany.cajeroautomatico.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.io.FileWriter;

/**
 * Editor y visualizador de vouchers con opciones de impresión y guardado
 */
public class VoucherEditorDialog extends JDialog {
    
    private JTextArea txtVoucher;
    private String contenidoVoucher;
    
    public VoucherEditorDialog(Frame parent, String voucher) {
        super(parent, "Voucher - Editor e Impresión", true);
        this.contenidoVoucher = voucher;
        
        inicializarComponentes();
        setSize(600, 700);
        setLocationRelativeTo(parent);
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 102, 204));
        panelTitulo.setBorder(new EmptyBorder(15, 10, 15, 10));
        
        JLabel lblTitulo = new JLabel("📄 VISUALIZAR Y GESTIONAR VOUCHER");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Área de texto para el voucher
        txtVoucher = new JTextArea(contenidoVoucher);
        txtVoucher.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtVoucher.setEditable(false);
        txtVoucher.setBackground(Color.WHITE);
        txtVoucher.setMargin(new Insets(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(txtVoucher);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));
        panelBotones.setBorder(new EmptyBorder(10, 10, 20, 10));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnImprimir = crearBoton("🖨️ Imprimir", new Color(52, 152, 219));
        JButton btnGuardar = crearBoton("💾 Guardar", new Color(46, 204, 113));
        JButton btnCopiar = crearBoton("📋 Copiar", new Color(155, 89, 182));
        JButton btnCerrar = crearBoton("✖ Cerrar", new Color(231, 76, 60));
        
        btnImprimir.addActionListener(e -> imprimirVoucher());
        btnGuardar.addActionListener(e -> guardarVoucher());
        btnCopiar.addActionListener(e -> copiarAlPortapapeles());
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnImprimir);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCopiar);
        panelBotones.add(btnCerrar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        getContentPane().setBackground(Color.WHITE);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efectos hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    private void imprimirVoucher() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }
                
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                
                g2d.setFont(new Font("Courier New", Font.PLAIN, 10));
                
                String[] lineas = contenidoVoucher.split("\n");
                int y = 50;
                for (String linea : lineas) {
                    g2d.drawString(linea, 50, y);
                    y += 15;
                }
                
                return PAGE_EXISTS;
            }
        });
        
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this,
                    "Voucher enviado a la impresora correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al imprimir: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void guardarVoucher() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Voucher");
        fileChooser.setSelectedFile(new File("voucher_" + System.currentTimeMillis() + ".txt"));
        
        int resultado = fileChooser.showSaveDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(contenidoVoucher);
                JOptionPane.showMessageDialog(this,
                    "Voucher guardado exitosamente en:\n" + archivo.getAbsolutePath(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void copiarAlPortapapeles() {
        txtVoucher.selectAll();
        txtVoucher.copy();
        txtVoucher.setSelectionEnd(0);
        JOptionPane.showMessageDialog(this,
            "Voucher copiado al portapapeles.",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
    }
}