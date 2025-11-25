package com.mycompany.cajeroautomatico.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.mycompany.cajeroautomatico.controlador.CajeroController;

/**
 * Vista principal del cajero automático - Interfaz mejorada y profesional
 */
public class CajeroAutomaticoView extends JFrame implements KeyListener {
    
    // Componentes UI
    private JTextArea pantallaPrincipal;
    private JLabel lblFechaHora;
    private JLabel lblEstado;
    private JPanel panelTeclado;
    private StringBuilder entradaActual;
    
    // Controlador
    private CajeroController controlador;
    
    // Botones de operaciones
    private JButton btnConsultar, btnRetirar, btnDepositar, btnTransferir;
    private JButton btnHistorial, btnCambiarPin;
    private JToggleButton btnSwitchTema;
    private JButton btnCrearCuenta, btnSalir, btnAyuda;
    private JButton btnBorrar, btnAceptar;
    
    // Paleta de colores (será modificada por el tema)
    private Color colorPrimario, colorSecundario, colorExito, colorPeligro, colorAdvertencia, colorFondo;
    private Color colorTexto, colorPanel, colorBorde;
    private boolean esModoOscuro = false;
    
    public CajeroAutomaticoView() {
        entradaActual = new StringBuilder();
        controlador = new CajeroController(this);
        
        configurarTema(esModoOscuro);
        
        inicializarVentana();
        crearPanelSuperior();
        crearPanelCentral();
        crearPanelBotonesLaterales();
        crearPanelInferior();
        
        iniciarReloj();
        mostrarPantallaBienvenida();
        
        actualizarUITema();
    }
    
    private void inicializarVentana() {
        setTitle("Banco MultiRed - Cajero Automático v2.0");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(null);
        
        // Habilitar escucha de teclado
        addKeyListener(this);
        setFocusable(true);
        
        // Icono de la aplicación
        try {
            setIconImage(new ImageIcon(new URL(
                "https://cdn-icons-png.flaticon.com/512/2830/2830284.png"
            )).getImage());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono");
        }
    }
    
    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Logo y título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        
        try {
            ImageIcon iconLogo = new ImageIcon(new URL(
                "https://cdn-icons-png.flaticon.com/512/2830/2830284.png"
            ));
            Image img = iconLogo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(img));
            panelTitulo.add(lblLogo);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo");
        }
        
        JLabel lblTitulo = new JLabel("BANCO MULTIRED");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        
        // Estado del sistema
        lblEstado = new JLabel("SISTEMA ACTIVO");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panelSuperior.add(panelTitulo, BorderLayout.WEST);
        panelSuperior.add(lblEstado, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
    }
    
    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Pantalla principal
        pantallaPrincipal = new JTextArea();
        pantallaPrincipal.setEditable(false);
        pantallaPrincipal.setFont(new Font("Consolas", Font.BOLD, 16));
        pantallaPrincipal.setMargin(new Insets(30, 30, 30, 30));
        pantallaPrincipal.setLineWrap(true);
        pantallaPrincipal.setWrapStyleWord(true);
        
        JScrollPane scrollPantalla = new JScrollPane(pantallaPrincipal);
        
        panelCentral.add(scrollPantalla, BorderLayout.CENTER);
        
        // Panel decorativo con GIF
        JPanel panelDecorativo = new JPanel(new BorderLayout());
        panelDecorativo.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelDecorativo.setPreferredSize(new Dimension(250, 0));
        
        try {
            ImageIcon iconGif = new ImageIcon("C:/Users/UPSJB/Downloads/cajero-automatico.gif");
            JLabel lblGif = new JLabel(iconGif);
            lblGif.setHorizontalAlignment(SwingConstants.CENTER);
            panelDecorativo.add(lblGif, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel lblPlaceholder = new JLabel("BANCO", SwingConstants.CENTER);
            lblPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 72));
            panelDecorativo.add(lblPlaceholder, BorderLayout.CENTER);
        }
        
        JLabel lblInfo = new JLabel("<html><center>Banca Segura<br>24/7</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        panelDecorativo.add(lblInfo, BorderLayout.SOUTH);
        
        panelCentral.add(panelDecorativo, BorderLayout.EAST);
        
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private void crearPanelBotonesLaterales() {
        // Panel izquierdo
        JPanel panelIzquierdo = new JPanel(new GridLayout(3, 1, 10, 15));
        panelIzquierdo.setBorder(new EmptyBorder(10, 10, 10, 5));
        
        btnConsultar = crearBotonOperacion("CONSULTAR SALDO", colorSecundario);
        btnRetirar = crearBotonOperacion("RETIRAR DINERO", colorPrimario);
        btnHistorial = crearBotonOperacion("VER HISTORIAL", new Color(52, 73, 94));
        
        btnConsultar.addActionListener(e -> controlador.consultarSaldo());
        btnRetirar.addActionListener(e -> controlador.realizarRetiro());
        btnHistorial.addActionListener(e -> controlador.verHistorial());
        
        panelIzquierdo.add(btnConsultar);
        panelIzquierdo.add(btnRetirar);
        panelIzquierdo.add(btnHistorial);
        
        // Panel derecho
        JPanel panelDerecho = new JPanel(new GridLayout(3, 1, 10, 15));
        panelDerecho.setBorder(new EmptyBorder(10, 5, 10, 10));
        
        btnDepositar = crearBotonOperacion("DEPOSITAR DINERO", colorExito);
        btnTransferir = crearBotonOperacion("TRANSFERIR", colorAdvertencia);
        btnCambiarPin = crearBotonOperacion("CAMBIAR PIN", new Color(155, 89, 182));
        
        btnDepositar.addActionListener(e -> controlador.realizarDeposito());
        btnTransferir.addActionListener(e -> controlador.realizarTransferencia());
        btnCambiarPin.addActionListener(e -> controlador.cambiarPin());
        
        panelDerecho.add(btnDepositar);
        panelDerecho.add(btnTransferir);
        panelDerecho.add(btnCambiarPin);
        
        deshabilitarBotonesOperacion();
        
        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.EAST);
    }
    
    // Continúa en la Parte 2...
    
    public JTextArea getPantallaPrincipal() { return pantallaPrincipal; }
    public CajeroController getControlador() { return controlador; }
    public StringBuilder getEntradaActual() { return entradaActual; }   
  
    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBorder(new EmptyBorder(10, 10, 20, 10));
        
        // Teclado numérico
        panelTeclado = new JPanel(new GridLayout(4, 3, 8, 8));
        
        for (int i = 1; i <= 9; i++) {
            agregarBotonNumerico(String.valueOf(i));
        }
        
        btnBorrar = new JButton("BORRAR");
        btnBorrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBorrar.setForeground(Color.WHITE);
        btnBorrar.setFocusPainted(false);
        btnBorrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBorrar.addActionListener(e -> {
            entradaActual.setLength(0);
            actualizarPantallaEntrada();
        });
        
        agregarBotonNumerico("0");
        
        btnAceptar = new JButton("OK");
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAceptar.addActionListener(e -> controlador.procesarEntrada());
        
        panelTeclado.add(btnBorrar);
        panelTeclado.add(btnAceptar);
        
        // Panel de información y controles
        JPanel panelControles = new JPanel(new GridLayout(0, 1, 5, 8));
        panelControles.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        lblFechaHora = new JLabel("", SwingConstants.CENTER);
        lblFechaHora.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        btnCrearCuenta = new JButton("CREAR CUENTA NUEVA");
        btnCrearCuenta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCrearCuenta.setForeground(Color.WHITE);
        btnCrearCuenta.setFocusPainted(false);
        btnCrearCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCrearCuenta.addActionListener(e -> controlador.crearCuentaNueva());
        
        btnSalir = new JButton("SALIR / CERRAR SESIÓN");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> controlador.cerrarSesion());
        
        btnAyuda = new JButton("AYUDA");
        btnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAyuda.setForeground(Color.WHITE);
        btnAyuda.setFocusPainted(false);
        btnAyuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAyuda.addActionListener(e -> mostrarAyuda());
        
        btnSwitchTema = new JToggleButton("Modo Oscuro");
        btnSwitchTema.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSwitchTema.setFocusPainted(false);
        btnSwitchTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSwitchTema.addActionListener(e -> toggleTema());
        
        panelControles.add(lblFechaHora);
        panelControles.add(btnCrearCuenta);
        panelControles.add(btnSalir);
        panelControles.add(btnAyuda);
        panelControles.add(btnSwitchTema);
        
        panelInferior.add(panelTeclado, BorderLayout.CENTER);
        panelInferior.add(panelControles, BorderLayout.EAST);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JButton crearBotonOperacion(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(220, 60));
        boton.setFocusPainted(false);
        boton.setBorder(new RoundedBorder(20)); // Radio de 20 píxeles
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (boton.isEnabled()) {
                    boton.setBackground(color.brighter());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    private void agregarBotonNumerico(String numero) {
        JButton boton = new JButton(numero);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addActionListener(e -> {
            entradaActual.append(numero);
            actualizarPantallaEntrada();
        });
        
        panelTeclado.add(boton);
    }
    
    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  •  HH:mm:ss");
            lblFechaHora.setText(sdf.format(new Date()));
        });
        timer.start();
    }
    
    public void actualizarPantallaEntrada() {
        if (!controlador.haySesionActiva()) {
            String entrada = entradaActual.toString();
            String enmascarado = entrada.isEmpty() ? "" : "•".repeat(entrada.length());
            pantallaPrincipal.setText(String.format(
                "\n\n   INICIO DE SESIÓN\n\n" +
                "   Ingrese su DNI:\n   %s\n\n" +
                "   Luego presione OK", 
                enmascarado.isEmpty() ? "_______________" : enmascarado
            ));
        }
    }
    
    public void mostrarPantallaBienvenida() {
        pantallaPrincipal.setText(
            "\n\n" +
            "   ═════════════════════════════════════\n" +
            "        BIENVENIDO A MULTIRED\n" +
            "   ═════════════════════════════════════\n\n" +
            "   • Ingrese su DNI usando el teclado\n" +
            "   • O cree una cuenta nueva\n\n" +
            "   Sistema bancario seguro 24/7\n" +
            "   ═════════════════════════════════════"
        );
    }
    
    public void mostrarPantallaSesionIniciada(String nombreUsuario) {
        pantallaPrincipal.setText(String.format(
            "\n\n" +
            "   ═════════════════════════════════════\n" +
            "        SESIÓN INICIADA\n" +
            "   ═════════════════════════════════════\n\n" +
            "   Bienvenido/a:\n" +
            "   %s\n\n" +
            "   Seleccione una operación usando\n" +
            "   los botones laterales.\n" +
            "   ═════════════════════════════════════",
            nombreUsuario.toUpperCase()
        ));
        habilitarBotonesOperacion();
        actualizarEstado("SESIÓN ACTIVA", COLOR_EXITO);
    }
    
    public void habilitarBotonesOperacion() {
        btnConsultar.setEnabled(true);
        btnRetirar.setEnabled(true);
        btnDepositar.setEnabled(true);
        btnTransferir.setEnabled(true);
        btnHistorial.setEnabled(true);
        btnCambiarPin.setEnabled(true);
    }
    
    public void deshabilitarBotonesOperacion() {
        btnConsultar.setEnabled(false);
        btnRetirar.setEnabled(false);
        btnDepositar.setEnabled(false);
        btnTransferir.setEnabled(false);
        btnHistorial.setEnabled(false);
        btnCambiarPin.setEnabled(false);
    }
    
    public void actualizarEstado(String texto, Color color) {
        lblEstado.setText(texto);
        lblEstado.setForeground(color);
    }
    
    private void mostrarAyuda() {
        String ayuda = 
            "═══ GUÍA DE USO - CAJERO AUTOMÁTICO ═══\n\n" +
            "1. INICIAR SESIÓN:\n" +
            "   • Ingrese su DNI usando el teclado numérico\n" +
            "   • Presione OK y luego ingrese su PIN\n\n" +
            "2. OPERACIONES DISPONIBLES:\n" +
            "   • Consultar saldo\n" +
            "   • Retirar dinero\n" +
            "   • Depositar dinero\n" +
            "   • Transferir a otra cuenta\n" +
            "   • Ver historial de transacciones\n" +
            "   • Cambiar PIN de seguridad\n\n" +
            "3. CREAR CUENTA:\n" +
            "   • Use el botón 'Crear Cuenta Nueva'\n" +
            "   • Complete el formulario de registro\n" +
            "   • Resuelva el captcha de seguridad\n\n" +
            "4. VOUCHERS:\n" +
            "   • Podrá imprimir, guardar o copiar\n" +
            "   • Los vouchers después de cada operación\n\n" +
            "Para soporte: soporte@bancomultired.com.pe";
        
        JTextArea txtAyuda = new JTextArea(ayuda);
        txtAyuda.setEditable(false);
        txtAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtAyuda);
        scroll.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scroll, 
            "Ayuda - Cajero Automático", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    // --- LÓGICA DE TEMAS ---

    private void toggleTema() {
        esModoOscuro = !esModoOscuro;
        configurarTema(esModoOscuro);
        actualizarUITema();
    }

    private void configurarTema(boolean oscuro) {
        if (oscuro) {
            // Paleta Modo Oscuro
            colorPrimario = new Color(23, 32, 42);
            colorSecundario = new Color(46, 64, 83);
            colorExito = new Color(35, 155, 86);
            colorPeligro = new Color(192, 57, 43);
            colorAdvertencia = new Color(211, 84, 0);
            colorFondo = new Color(52, 73, 94);
            colorTexto = new Color(236, 240, 241);
            colorPanel = new Color(44, 62, 80);
            colorBorde = new Color(128, 139, 150);
        } else {
            // Paleta Modo Claro (Original)
            colorPrimario = new Color(0, 102, 204);
            colorSecundario = new Color(41, 128, 185);
            colorExito = new Color(46, 204, 113);
            colorPeligro = new Color(231, 76, 60);
            colorAdvertencia = new Color(243, 156, 18);
            colorFondo = new Color(236, 240, 241);
            colorTexto = new Color(44, 62, 80);
            colorPanel = Color.WHITE;
            colorBorde = colorSecundario;
        }
    }

    private void actualizarUITema() {
        // Ventana principal
        getContentPane().setBackground(colorFondo);

        // Panel Superior
        ((JPanel) getRootPane().getContentPane().getComponent(0)).setBackground(colorPrimario);
        ((JPanel) ((JPanel) getRootPane().getContentPane().getComponent(0)).getComponent(0)).setBackground(colorPrimario); // panelTitulo
        lblEstado.setForeground(colorExito);

        // Panel Central
        JPanel panelCentral = (JPanel) ((JPanel) getRootPane().getContentPane().getComponent(1)).getComponent(0);
        panelCentral.setBackground(colorFondo);
        pantallaPrincipal.setBackground(colorPanel);
        pantallaPrincipal.setForeground(colorTexto);
        ((JScrollPane) panelCentral.getComponent(0)).setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorde, 3),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JPanel panelDecorativo = (JPanel) panelCentral.getComponent(1);
        panelDecorativo.setBackground(colorPanel);
            actualizarPantallaEntrada();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita implementación
    }

    // Clase interna para el borde redondeado
    private static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getBackground());
            g2.fill(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2.setColor(c.getForeground());
            g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            CajeroAutomaticoView vista = new CajeroAutomaticoView();
            vista.setVisible(true);
        });
    }
}
