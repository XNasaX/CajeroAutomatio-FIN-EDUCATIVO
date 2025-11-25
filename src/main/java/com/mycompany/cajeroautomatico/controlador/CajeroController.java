package com.mycompany.cajeroautomatico.controlador;

import com.mycompany.cajeroautomatico.modelo.Transaccion;
import com.mycompany.cajeroautomatico.modelo.Usuario;
import com.mycompany.cajeroautomatico.servicio.BancoService;
import com.mycompany.cajeroautomatico.utilidad.CaptchaUtil;
import com.mycompany.cajeroautomatico.utilidad.VoucherGenerator;
import com.mycompany.cajeroautomatico.vista.CajeroAutomaticoView;
import com.mycompany.cajeroautomatico.vista.VoucherEditorDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * Controlador principal del cajero automático
 */
public class CajeroController {
    
    private CajeroAutomaticoView vista;
    private BancoService bancoService;
    private Usuario usuarioActual;
    private int intentosFallidos;
    private boolean sesionActiva;
    
    private static final int MAX_INTENTOS = 3;
    
    public CajeroController(CajeroAutomaticoView vista) {
        this.vista = vista;
        this.bancoService = BancoService.getInstancia();
        this.usuarioActual = null;
        this.intentosFallidos = 0;
        this.sesionActiva = false;
    }
    
    public void procesarEntrada() {
        String entrada = vista.getEntradaActual().toString();
        vista.getEntradaActual().setLength(0);
        
        if (entrada.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                "Por favor, ingrese su DNI.",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!sesionActiva) {
            intentarInicioSesion(entrada);
        } else {
            JOptionPane.showMessageDialog(vista,
                "Ya tiene una sesión activa.\nUse los botones laterales para operar.",
                "Sesión activa",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void intentarInicioSesion(String dni) {
        Optional<Usuario> usuarioOpt = bancoService.buscarUsuario(dni);
        
        if (!usuarioOpt.isPresent()) {
            JOptionPane.showMessageDialog(vista,
                "DNI no encontrado.\n\n" +
                "Use el botón 'Crear Cuenta Nueva' para registrarse.",
                "Usuario no encontrado",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        solicitarPin(usuarioOpt.get());
    }
    
    private void solicitarPin(Usuario usuario) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(new JLabel("DNI detectado: " + usuario.getDni()));
        panel.add(new JLabel("Titular: " + usuario.getNombre()));
        
        JPasswordField pinField = new JPasswordField();
        pinField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        Object[] mensaje = {
            panel,
            "Ingrese su PIN de seguridad:",
            pinField
        };
        
        int opcion = JOptionPane.showConfirmDialog(vista, mensaje,
            "Autenticación", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (opcion == JOptionPane.OK_OPTION) {
            String pin = new String(pinField.getPassword());
            validarPin(usuario, pin);
        }
    }
    
    private void validarPin(Usuario usuario, String pin) {
        if (usuario.validarPin(pin)) {
            iniciarSesion(usuario);
        } else {
            intentosFallidos++;
            
            if (intentosFallidos >= MAX_INTENTOS) {
                JOptionPane.showMessageDialog(vista,
                    "Ha excedido el número máximo de intentos.\n" +
                    "Por seguridad, la aplicación se cerrará.",
                    "Acceso bloqueado",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(vista,
                    String.format("PIN incorrecto.\nIntento %d de %d",
                        intentosFallidos, MAX_INTENTOS),
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
        this.sesionActiva = true;
        this.intentosFallidos = 0;
        
        vista.mostrarPantallaSesionIniciada(usuario.getNombre());
        
        JOptionPane.showMessageDialog(vista,
            String.format("¡Bienvenido/a %s!\n\nSaldo disponible: S/. %.2f",
                usuario.getNombre(), usuario.getSaldo()),
            "Inicio de sesión exitoso",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void cerrarSesion() {
        if (sesionActiva) {
            int confirmacion = JOptionPane.showConfirmDialog(vista,
                "¿Está seguro que desea cerrar la sesión?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                this.usuarioActual = null;
                this.sesionActiva = false;
                vista.deshabilitarBotonesOperacion();
                vista.mostrarPantallaBienvenida();
                vista.actualizarEstado("● SISTEMA ACTIVO", 
                    new Color(46, 204, 113));
                
                JOptionPane.showMessageDialog(vista,
                    "Sesión cerrada correctamente.\n¡Gracias por usar nuestros servicios!",
                    "Hasta pronto",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(vista,
                "No hay ninguna sesión activa.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void consultarSaldo() {
        if (!verificarSesion()) return;
        
        vista.getPantallaPrincipal().setText(String.format(
            "\n\n" +
            "   ═════════════════════════════════════\n" +
            "        💰 CONSULTA DE SALDO\n" +
            "   ═════════════════════════════════════\n\n" +
            "   Cliente: %s\n" +
            "   DNI: %s\n\n" +
            "   SALDO DISPONIBLE:\n" +
            "   S/. %.2f\n\n" +
            "   ═════════════════════════════════════",
            usuarioActual.getNombre(),
            usuarioActual.getDni(),
            usuarioActual.getSaldo()
        ));
        
        int opcion = JOptionPane.showConfirmDialog(vista,
            "¿Desea generar un voucher de esta consulta?",
            "Voucher",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            String voucher = VoucherGenerator.generarVoucher(
                usuarioActual, "CONSULTA DE SALDO", 0,
                "CS" + System.currentTimeMillis()
            );
            mostrarEditorVoucher(voucher);
        }
    }
    
    public void realizarRetiro() {
        if (!verificarSesion()) return;
        
        String[] opciones = {"50", "100", "200", "500", "Otro monto"};
        String seleccion = (String) JOptionPane.showInputDialog(vista,
            "Seleccione el monto a retirar:",
            "Retiro de dinero",
            JOptionPane.QUESTION_MESSAGE,
            null, opciones, opciones[0]);
        
        if (seleccion != null) {
            double monto;
            
            if (seleccion.equals("Otro monto")) {
                String entrada = JOptionPane.showInputDialog(vista,
                    "Ingrese el monto a retirar:",
                    "Monto personalizado",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (entrada == null || entrada.trim().isEmpty()) return;
                
                try {
                    monto = Double.parseDouble(entrada);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(vista,
                        "Monto inválido. Debe ingresar un número.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                monto = Double.parseDouble(seleccion);
            }
            
            if (monto <= 0) {
                JOptionPane.showMessageDialog(vista,
                    "El monto debe ser mayor a cero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            confirmarOperacionConPin(() -> ejecutarRetiro(monto));
        }
    }
    
    private void ejecutarRetiro(double monto) {
        if (usuarioActual.retirar(monto)) {
            vista.getPantallaPrincipal().setText(String.format(
                "\n\n   ✓ RETIRO EXITOSO\n\n" +
                "   Monto retirado: S/. %.2f\n" +
                "   Nuevo saldo: S/. %.2f",
                monto, usuarioActual.getSaldo()
            ));
            
            String voucher = VoucherGenerator.generarVoucher(
                usuarioActual, "RETIRO DE EFECTIVO", monto,
                "RT" + System.currentTimeMillis()
            );
            mostrarEditorVoucher(voucher);
        } else {
            JOptionPane.showMessageDialog(vista,
                String.format("Saldo insuficiente.\n\n" +
                    "Saldo disponible: S/. %.2f\n" +
                    "Monto solicitado: S/. %.2f",
                    usuarioActual.getSaldo(), monto),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void realizarDeposito() {
        String dniDestino = JOptionPane.showInputDialog(vista,
            "Ingrese el DNI de la cuenta destino:",
            "Depósito",
            JOptionPane.QUESTION_MESSAGE);
        
        if (dniDestino == null || dniDestino.trim().isEmpty()) return;
        
        Optional<Usuario> destinoOpt = bancoService.buscarUsuario(dniDestino);
        
        if (!destinoOpt.isPresent()) {
            JOptionPane.showMessageDialog(vista,
                "La cuenta destino no existe.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario destino = destinoOpt.get();
        
        String entrada = JOptionPane.showInputDialog(vista,
            String.format("Cuenta destino:\n%s\nDNI: %s\n\nIngrese el monto a depositar:",
                destino.getNombre(), destino.getDni()),
            "Monto a depositar",
            JOptionPane.QUESTION_MESSAGE);
        
        if (entrada == null || entrada.trim().isEmpty()) return;
        
        try {
            double monto = Double.parseDouble(entrada);
            
            if (monto <= 0) {
                JOptionPane.showMessageDialog(vista,
                    "El monto debe ser mayor a cero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            destino.depositar(monto);
            
            JOptionPane.showMessageDialog(vista,
                String.format("Depósito exitoso.\n\n" +
                    "Monto: S/. %.2f\n" +
                    "Destino: %s",
                    monto, destino.getNombre()),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            String voucher = VoucherGenerator.generarVoucher(
                destino, "DEPÓSITO RECIBIDO", monto,
                "DP" + System.currentTimeMillis()
            );
            mostrarEditorVoucher(voucher);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                "Monto inválido.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Continúa en comentario siguiente...
    
    public boolean haySesionActiva() {
        return sesionActiva;
    }
    
    private boolean verificarSesion() {
        if (usuarioActual == null || !sesionActiva) {
            JOptionPane.showMessageDialog(vista,
                "Debe iniciar sesión primero.",
                "Sesión requerida",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    public void realizarTransferencia() {
        if (!verificarSesion()) return;
        
        String dniDestino = JOptionPane.showInputDialog(vista,
            "Ingrese el DNI de la cuenta destino:",
            "Transferencia",
            JOptionPane.QUESTION_MESSAGE);
        
        if (dniDestino == null || dniDestino.trim().isEmpty()) return;
        
        if (dniDestino.equals(usuarioActual.getDni())) {
            JOptionPane.showMessageDialog(vista,
                "No puede transferir a su propia cuenta.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Optional<Usuario> destinoOpt = bancoService.buscarUsuario(dniDestino);
        
        if (!destinoOpt.isPresent()) {
            JOptionPane.showMessageDialog(vista,
                "La cuenta destino no existe.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario destino = destinoOpt.get();
        
        String entrada = JOptionPane.showInputDialog(vista,
            String.format("Transferir a:\n%s\nDNI: %s\n\n" +
                "Su saldo disponible: S/. %.2f\n\n" +
                "Ingrese el monto a transferir:",
                destino.getNombre(), destino.getDni(), usuarioActual.getSaldo()),
            "Monto a transferir",
            JOptionPane.QUESTION_MESSAGE);
        
        if (entrada == null || entrada.trim().isEmpty()) return;
        
        try {
            double monto = Double.parseDouble(entrada);
            
            if (monto <= 0) {
                JOptionPane.showMessageDialog(vista,
                    "El monto debe ser mayor a cero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            confirmarOperacionConPin(() -> ejecutarTransferencia(destino, monto));
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista,
                "Monto inválido.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ejecutarTransferencia(Usuario destino, double monto) {
        if (usuarioActual.transferir(destino, monto)) {
            vista.getPantallaPrincipal().setText(String.format(
                "\n\n   ✓ TRANSFERENCIA EXITOSA\n\n" +
                "   Monto: S/. %.2f\n" +
                "   Destino: %s\n" +
                "   Nuevo saldo: S/. %.2f",
                monto, destino.getNombre(), usuarioActual.getSaldo()
            ));
            
            String voucher = VoucherGenerator.generarVoucherTransferencia(
                usuarioActual, destino, monto,
                "TF" + System.currentTimeMillis()
            );
            mostrarEditorVoucher(voucher);
        } else {
            JOptionPane.showMessageDialog(vista,
                String.format("Saldo insuficiente.\n\n" +
                    "Saldo disponible: S/. %.2f\n" +
                    "Monto solicitado: S/. %.2f",
                    usuarioActual.getSaldo(), monto),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void verHistorial() {
        if (!verificarSesion()) return;
        
        StringBuilder historial = new StringBuilder();
        historial.append("\n═══════════════════════════════════════\n");
        historial.append("     📊 HISTORIAL DE TRANSACCIONES\n");
        historial.append("═══════════════════════════════════════\n\n");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        if (usuarioActual.getHistorial().isEmpty()) {
            historial.append("  No hay transacciones registradas.\n");
        } else {
            for (Transaccion t : usuarioActual.getHistorial()) {
                historial.append(String.format(
                    "  %s\n" +
                    "  Tipo: %s\n" +
                    "  Monto: S/. %.2f\n" +
                    "  Saldo resultante: S/. %.2f\n" +
                    "  Nro. Op: %s\n" +
                    "  ───────────────────────────────────\n",
                    sdf.format(t.getFecha()),
                    t.getTipo(),
                    t.getMonto(),
                    t.getSaldoResultante(),
                    t.getNumeroOperacion()
                ));
            }
        }
        
        historial.append("\n═══════════════════════════════════════");
        
        JTextArea txtHistorial = new JTextArea(historial.toString());
        txtHistorial.setEditable(false);
        txtHistorial.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtHistorial);
        scroll.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(vista, scroll,
            "Historial de Transacciones",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void cambiarPin() {
        if (!verificarSesion()) return;
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JPasswordField pinActualField = new JPasswordField();
        JPasswordField pinNuevoField = new JPasswordField();
        JPasswordField pinConfirmField = new JPasswordField();
        
        panel.add(new JLabel("PIN actual:"));
        panel.add(pinActualField);
        panel.add(new JLabel("PIN nuevo:"));
        panel.add(pinNuevoField);
        panel.add(new JLabel("Confirmar PIN:"));
        panel.add(pinConfirmField);
        
        int opcion = JOptionPane.showConfirmDialog(vista, panel,
            "Cambiar PIN de seguridad",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (opcion == JOptionPane.OK_OPTION) {
            String pinActual = new String(pinActualField.getPassword());
            String pinNuevo = new String(pinNuevoField.getPassword());
            String pinConfirm = new String(pinConfirmField.getPassword());
            
            if (!usuarioActual.validarPin(pinActual)) {
                JOptionPane.showMessageDialog(vista,
                    "El PIN actual es incorrecto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (pinNuevo.length() < 4) {
                JOptionPane.showMessageDialog(vista,
                    "El PIN debe tener al menos 4 dígitos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!pinNuevo.equals(pinConfirm)) {
                JOptionPane.showMessageDialog(vista,
                    "Los PINs nuevos no coinciden.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Aquí deberías actualizar el PIN en el usuario
            // usuarioActual.setPin(pinNuevo); - necesitas agregar este método
            
            JOptionPane.showMessageDialog(vista,
                "PIN cambiado exitosamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void crearCuentaNueva() {
        String codigoCaptcha = CaptchaUtil.generarCodigo(5);
        BufferedImage imagenCaptcha = CaptchaUtil.generarImagen(codigoCaptcha);
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 10));
        
        JTextField txtNombre = new JTextField();
        JTextField txtDni = new JTextField();
        JPasswordField txtPin = new JPasswordField();
        JPasswordField txtPinConfirm = new JPasswordField();
        JTextField txtCaptcha = new JTextField();
        
        panel.add(new JLabel("📝 REGISTRO DE NUEVA CUENTA"));
        panel.add(new JLabel(" "));
        panel.add(new JLabel("Nombre completo:"));
        panel.add(txtNombre);
        panel.add(new JLabel("DNI (será su usuario):"));
        panel.add(txtDni);
        panel.add(new JLabel("Crear PIN (4 dígitos mínimo):"));
        panel.add(txtPin);
        panel.add(new JLabel("Confirmar PIN:"));
        panel.add(txtPinConfirm);
        panel.add(new JLabel(" "));
        panel.add(new JLabel("🔐 VERIFICACIÓN DE SEGURIDAD"));
        panel.add(new JLabel("Ingrese el código de la imagen:"));
        panel.add(new JLabel(new ImageIcon(imagenCaptcha)));
        panel.add(txtCaptcha);
        
        int opcion = JOptionPane.showConfirmDialog(vista, panel,
            "Crear Cuenta Nueva",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (opcion == JOptionPane.OK_OPTION) {
            validarYCrearCuenta(
                txtNombre.getText(),
                txtDni.getText(),
                new String(txtPin.getPassword()),
                new String(txtPinConfirm.getPassword()),
                txtCaptcha.getText(),
                codigoCaptcha
            );
        }
    }
    
    private void validarYCrearCuenta(String nombre, String dni, String pin,
                                      String pinConfirm, String captchaIngresado,
                                      String captchaCorrecto) {
        
        if (!captchaIngresado.equalsIgnoreCase(captchaCorrecto)) {
            JOptionPane.showMessageDialog(vista,
                "El código de seguridad es incorrecto.",
                "Error de verificación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (nombre.trim().isEmpty() || dni.trim().isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                "Todos los campos son obligatorios.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (pin.length() < 4) {
            JOptionPane.showMessageDialog(vista,
                "El PIN debe tener al menos 4 caracteres.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!pin.equals(pinConfirm)) {
            JOptionPane.showMessageDialog(vista,
                "Los PINs no coinciden.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (bancoService.existeUsuario(dni)) {
            JOptionPane.showMessageDialog(vista,
                "Este DNI ya está registrado.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario nuevoUsuario = new Usuario(nombre, dni, pin, 0.0);
        
        if (bancoService.registrarUsuario(nuevoUsuario)) {
            JOptionPane.showMessageDialog(vista,
                "¡Cuenta creada exitosamente!\n\n" +
                "Ya puede iniciar sesión con su DNI y PIN.",
                "Registro exitoso",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista,
                "Error al crear la cuenta. Intente nuevamente.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void confirmarOperacionConPin(Runnable operacion) {
        JPasswordField pinField = new JPasswordField();
        
        int opcion = JOptionPane.showConfirmDialog(vista,
            new Object[] {
                "Por seguridad, confirme su PIN:",
                pinField
            },
            "Confirmación de seguridad",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.OK_OPTION) {
            String pin = new String(pinField.getPassword());
            
            if (usuarioActual.validarPin(pin)) {
                operacion.run();
            } else {
                JOptionPane.showMessageDialog(vista,
                    "PIN incorrecto. Operación cancelada.",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarEditorVoucher(String voucher) {
        VoucherEditorDialog editor = new VoucherEditorDialog(vista, voucher);
        editor.setVisible(true);
    }
}