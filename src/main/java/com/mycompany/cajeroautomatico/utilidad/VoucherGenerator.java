package com.mycompany.cajeroautomatico.utilidad;

import com.mycompany.cajeroautomatico.modelo.Usuario;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Generador de vouchers para operaciones bancarias
 */
public class VoucherGenerator {
    
    private static final String SEPARADOR = "═══════════════════════════════════════";
    private static final String LINEA = "───────────────────────────────────────";
    
    public static String generarVoucher(Usuario usuario, String tipoOperacion, 
                                       double monto, String numeroOperacion) {
        StringBuilder voucher = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        voucher.append("\n").append(SEPARADOR).append("\n");
        voucher.append("       🏦 BANCO MULTIRED S.A.\n");
        voucher.append("        COMPROBANTE DE OPERACIÓN\n");
        voucher.append(SEPARADOR).append("\n\n");
        
        voucher.append(String.format("  Operación:    %-20s\n", tipoOperacion));
        voucher.append(String.format("  Nro. Op.:     %-20s\n", numeroOperacion));
        voucher.append(LINEA).append("\n");
        
        voucher.append(String.format("  Cliente:      %-20s\n", usuario.getNombre()));
        voucher.append(String.format("  DNI:          %-20s\n", usuario.getDni()));
        voucher.append(LINEA).append("\n");
        
        if (monto > 0) {
            voucher.append(String.format("  Monto:        S/. %.2f\n", monto));
        }
        voucher.append(String.format("  Saldo Actual: S/. %.2f\n", usuario.getSaldo()));
        voucher.append(LINEA).append("\n");
        
        voucher.append(String.format("  Fecha:        %s\n", sdf.format(new Date())));
        voucher.append(LINEA).append("\n\n");
        
        voucher.append("  ¡Gracias por utilizar nuestros servicios!\n");
        voucher.append("     www.bancomultired.com.pe\n");
        voucher.append(SEPARADOR).append("\n");
        
        return voucher.toString();
    }
    
    public static String generarVoucherTransferencia(Usuario origen, Usuario destino,
                                                     double monto, String numeroOperacion) {
        StringBuilder voucher = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        voucher.append("\n").append(SEPARADOR).append("\n");
        voucher.append("       🏦 BANCO MULTIRED S.A.\n");
        voucher.append("      COMPROBANTE DE TRANSFERENCIA\n");
        voucher.append(SEPARADOR).append("\n\n");
        
        voucher.append(String.format("  Nro. Op.:     %-20s\n", numeroOperacion));
        voucher.append(LINEA).append("\n");
        
        voucher.append("  ORIGEN:\n");
        voucher.append(String.format("    Titular:    %-20s\n", origen.getNombre()));
        voucher.append(String.format("    DNI:        %-20s\n", origen.getDni()));
        voucher.append("\n");
        
        voucher.append("  DESTINO:\n");
        voucher.append(String.format("    Titular:    %-20s\n", destino.getNombre()));
        voucher.append(String.format("    DNI:        %-20s\n", destino.getDni()));
        voucher.append(LINEA).append("\n");
        
        voucher.append(String.format("  Monto:        S/. %.2f\n", monto));
        voucher.append(String.format("  Saldo Actual: S/. %.2f\n", origen.getSaldo()));
        voucher.append(LINEA).append("\n");
        
        voucher.append(String.format("  Fecha:        %s\n", sdf.format(new Date())));
        voucher.append(LINEA).append("\n\n");
        
        voucher.append("  ¡Gracias por utilizar nuestros servicios!\n");
        voucher.append("     www.bancomultired.com.pe\n");
        voucher.append(SEPARADOR).append("\n");
        
        return voucher.toString();
    }
}