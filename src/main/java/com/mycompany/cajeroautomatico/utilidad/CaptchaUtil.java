package com.mycompany.cajeroautomatico.utilidad;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Utilidad para generar captchas de seguridad
 */
public class CaptchaUtil {
    
    public static String generarCodigo(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < longitud; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        
        return codigo.toString();
    }
    
    public static BufferedImage generarImagen(String codigo) {
        int ancho = 200;
        int alto = 70;
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagen.createGraphics();
        Random random = new Random();
        
        // Fondo con gradiente
        GradientPaint gradiente = new GradientPaint(
            0, 0, new Color(240, 248, 255),
            ancho, alto, new Color(230, 240, 250)
        );
        g2d.setPaint(gradiente);
        g2d.fillRect(0, 0, ancho, alto);
        
        // Líneas de ruido
        g2d.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < 8; i++) {
            g2d.setColor(new Color(
                180 + random.nextInt(50),
                180 + random.nextInt(50),
                180 + random.nextInt(50)
            ));
            g2d.drawLine(
                random.nextInt(ancho), random.nextInt(alto),
                random.nextInt(ancho), random.nextInt(alto)
            );
        }
        
        // Puntos de ruido
        for (int i = 0; i < 50; i++) {
            g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g2d.fillOval(random.nextInt(ancho), random.nextInt(alto), 2, 2);
        }
        
        // Texto del captcha
        g2d.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 32));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (ancho - fm.stringWidth(codigo)) / 2;
        int y = ((alto - fm.getHeight()) / 2) + fm.getAscent();
        
        // Sombra del texto
        g2d.setColor(new Color(100, 100, 100, 50));
        g2d.drawString(codigo, x + 2, y + 2);
        
        // Texto principal con colores variados
        for (int i = 0; i < codigo.length(); i++) {
            g2d.setColor(new Color(
                random.nextInt(100),
                random.nextInt(100),
                random.nextInt(150)
            ));
            g2d.drawString(String.valueOf(codigo.charAt(i)), x + (i * 30), y);
        }
        
        g2d.dispose();
        return imagen;
    }
}