package com.mycompany.cajeroautomatico.modelo;

import java.util.Date;

/**
 * Clase que representa una transacción bancaria
 */
public class Transaccion {
    private String tipo;
    private double monto;
    private double saldoResultante;
    private Date fecha;
    private String numeroOperacion;
    
    public Transaccion(String tipo, double monto, double saldoResultante, Date fecha) {
        this.tipo = tipo;
        this.monto = monto;
        this.saldoResultante = saldoResultante;
        this.fecha = fecha;
        this.numeroOperacion = generarNumeroOperacion();
    }
    
    private String generarNumeroOperacion() {
        return String.format("%d%04d", 
            System.currentTimeMillis() / 1000, 
            (int)(Math.random() * 10000));
    }
    
    // Getters
    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public double getSaldoResultante() { return saldoResultante; }
    public Date getFecha() { return fecha; }
    public String getNumeroOperacion() { return numeroOperacion; }
}