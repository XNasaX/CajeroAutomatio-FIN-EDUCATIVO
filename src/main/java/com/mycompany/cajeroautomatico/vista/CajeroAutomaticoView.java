package com.mycompany.cajeroautomatico.vista;

import com.mycompany.cajeroautomatico.controlador.CajeroController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Vista principal del cajero automático - Interfaz mejorada y profesional
 */
public class CajeroAutomaticoView extends JFrame {
    
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
    
    // Colores del tema
    private final Color COLOR_PRIMARIO = new Color(0, 102, 204);
    private final Color COLOR_SECUNDARIO = new Color(41, 128, 185);
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private final Color COLOR_ADVERTENCIA = new Color(243, 156, 18);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    
    public CajeroAutomaticoView() {
        entradaActual = new StringBuilder();
        controlador = new CajeroController(this);
        
        inicializarVentana();
        crearPanelSuperior();
        crearPanelCentral();
        crearPanelBotonesLaterales();
        crearPanelInferior();
        
        iniciarReloj();
        mostrarPantallaBienvenida();
    }
    
    private void inicializarVentana() {
        setTitle("Banco MultiRed - Cajero Automático v2.0");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(COLOR_FONDO);
        setLocationRelativeTo(null);
        
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
        panelSuperior.setBackground(COLOR_PRIMARIO);
        panelSuperior.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Logo y título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelTitulo.setBackground(COLOR_PRIMARIO);
        
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
        lblEstado = new JLabel("● SISTEMA ACTIVO");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEstado.setForeground(COLOR_EXITO);
        
        panelSuperior.add(panelTitulo, BorderLayout.WEST);
        panelSuperior.add(lblEstado, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
    }
    
    private void crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Pantalla principal
        pantallaPrincipal = new JTextArea();
        pantallaPrincipal.setEditable(false);
        pantallaPrincipal.setBackground(new Color(44, 62, 80));
        pantallaPrincipal.setForeground(new Color(236, 240, 241));
        pantallaPrincipal.setFont(new Font("Consolas", Font.BOLD, 16));
        pantallaPrincipal.setMargin(new Insets(30, 30, 30, 30));
        pantallaPrincipal.setLineWrap(true);
        pantallaPrincipal.setWrapStyleWord(true);
        
        JScrollPane scrollPantalla = new JScrollPane(pantallaPrincipal);
        scrollPantalla.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_SECUNDARIO, 3),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        panelCentral.add(scrollPantalla, BorderLayout.CENTER);
        
        // Panel decorativo con GIF
        JPanel panelDecorativo = new JPanel(new BorderLayout());
        panelDecorativo.setBackground(Color.WHITE);
        panelDecorativo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_SECUNDARIO, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        panelDecorativo.setPreferredSize(new Dimension(250, 0));
        
        try {
            ImageIcon iconGif = new ImageIcon(new URL(
                "https://media.tenor.com/GfSX-u7u_2EAAAAi/credit-card.gif"
            ));
            JLabel lblGif = new JLabel(iconGif);
            lblGif.setHorizontalAlignment(SwingConstants.CENTER);
            panelDecorativo.add(lblGif, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel lblPlaceholder = new JLabel("💳", SwingConstants.CENTER);
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
        panelIzquierdo.setBackground(COLOR_FONDO);
        panelIzquierdo.setBorder(new EmptyBorder(10, 10, 10, 5));
        
        btnConsultar = crearBotonOperacion("💰 CONSULTAR SALDO", COLOR_SECUNDARIO);
        btnRetirar = crearBotonOperacion("💵 RETIRAR DINERO", COLOR_PRIMARIO);
        btnHistorial = crearBotonOperacion("📊 VER HISTORIAL", new Color(52, 73, 94));
        
        btnConsultar.addActionListener(e -> controlador.consultarSaldo());
        btnRetirar.addActionListener(e -> controlador.realizarRetiro());
        btnHistorial.addActionListener(e -> controlador.verHistorial());
        
        panelIzquierdo.add(btnConsultar);
        panelIzquierdo.add(btnRetirar);
        panelIzquierdo.add(btnHistorial);
        
        // Panel derecho
        JPanel panelDerecho = new JPanel(new GridLayout(3, 1, 10, 15));
        panelDerecho.setBackground(COLOR_FONDO);
        panelDerecho.setBorder(new EmptyBorder(10, 5, 10, 10));
        
        btnDepositar = crearBotonOperacion("💳 DEPOSITAR DINERO", COLOR_EXITO);
        btnTransferir = crearBotonOperacion("🔄 TRANSFERIR", COLOR_ADVERTENCIA);
        btnCambiarPin = crearBotonOperacion("🔐 CAMBIAR PIN", new Color(155, 89, 182));
        
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
        panelInferior.setBackground(COLOR_FONDO);
        panelInferior.setBorder(new EmptyBorder(10, 10, 20, 10));
        
        // Teclado numérico
        panelTeclado = new JPanel(new GridLayout(4, 3, 8, 8));
        panelTeclado.setBackground(COLOR_FONDO);
        
        for (int i = 1; i <= 9; i++) {
            agregarBotonNumerico(String.valueOf(i));
        }
        
        JButton btnBorrar = new JButton("⌫ BORRAR");
        btnBorrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBorrar.setBackground(COLOR_PELIGRO);
        btnBorrar.setForeground(Color.WHITE);
        btnBorrar.setFocusPainted(false);
        btnBorrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBorrar.addActionListener(e -> {
            entradaActual.setLength(0);
            actualizarPantallaEntrada();
        });
        
        agregarBotonNumerico("0");
        
        JButton btnAceptar = new JButton("✓ OK");
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAceptar.setBackground(COLOR_EXITO);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAceptar.addActionListener(e -> controlador.procesarEntrada());
        
        panelTeclado.add(btnBorrar);
        panelTeclado.add(btnAceptar);
        
        // Panel de información y controles
        JPanel panelControles = new JPanel(new GridLayout(4, 1, 5, 8));
        panelControles.setBackground(COLOR_FONDO);
        panelControles.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        lblFechaHora = new JLabel("", SwingConstants.CENTER);
        lblFechaHora.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFechaHora.setForeground(COLOR_PRIMARIO);
        lblFechaHora.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_SECUNDARIO),
            new EmptyBorder(8, 5, 8, 5)
        ));
        
        JButton btnCrearCuenta = new JButton("👤 CREAR CUENTA NUEVA");
        btnCrearCuenta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCrearCuenta.setBackground(COLOR_ADVERTENCIA);
        btnCrearCuenta.setForeground(Color.WHITE);
        btnCrearCuenta.setFocusPainted(false);
        btnCrearCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCrearCuenta.addActionListener(e -> controlador.crearCuentaNueva());
        
        JButton btnSalir = new JButton("🚪 SALIR / CERRAR SESIÓN");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setBackground(new Color(149, 165, 166));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> controlador.cerrarSesion());
        
        JButton btnAyuda = new JButton("❓ AYUDA");
        btnAyuda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAyuda.setBackground(new Color(52, 73, 94));
        btnAyuda.setForeground(Color.WHITE);
        btnAyuda.setFocusPainted(false);
        btnAyuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAyuda.addActionListener(e -> mostrarAyuda());
        
        panelControles.add(lblFechaHora);
        panelControles.add(btnCrearCuenta);
        panelControles.add(btnSalir);
        panelControles.add(btnAyuda);
        
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
        boton.setBorderPainted(false);
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
        boton.setBackground(Color.WHITE);
        boton.setForeground(COLOR_PRIMARIO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(COLOR_SECUNDARIO, 2));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addActionListener(e -> {
            entradaActual.append(numero);
            actualizarPantallaEntrada();
        });
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_FONDO);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE);
            }
        });
        
        panelTeclado.add(boton);
    }
    
    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  •  HH:mm:ss");
            lblFechaHora.setText("🕐 " + sdf.format(new Date()));
        });
        timer.start();
    }
    
    public void actualizarPantallaEntrada() {
        if (!controlador.haySesionActiva()) {
            String entrada = entradaActual.toString();
            String enmascarado = entrada.isEmpty() ? "" : "•".repeat(entrada.length());
            pantallaPrincipal.setText(String.format(
                "\n\n   🔐 INICIO DE SESIÓN\n\n" +
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
            "        🏦 BIENVENIDO A MULTIRED\n" +
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
            "        ✓ SESIÓN INICIADA\n" +
            "   ═════════════════════════════════════\n\n" +
            "   Bienvenido/a:\n" +
            "   %s\n\n" +
            "   Seleccione una operación usando\n" +
            "   los botones laterales.\n" +
            "   ═════════════════════════════════════",
            nombreUsuario.toUpperCase()
        ));
        habilitarBotonesOperacion();
        actualizarEstado("● SESIÓN ACTIVA", COLOR_EXITO);
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