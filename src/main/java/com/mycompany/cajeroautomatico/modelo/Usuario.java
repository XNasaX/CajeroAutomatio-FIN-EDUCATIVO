package com.mycompany.cajeroautomatico.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un usuario del cajero automático
 */
public class Usuario {
    private String nombre;
    private String dni;
    private String pin;
    private double saldo;
    private List<Transaccion> historial;
    
    public Usuario(String nombre, String dni, String pin, double saldo) {
        this.nombre = nombre;
        this.dni = dni;
        this.pin = pin;
        this.saldo = saldo;
        this.historial = new ArrayList<>();
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getPin() { return pin; }
    public double getSaldo() { return saldo; }
    public List<Transaccion> getHistorial() { return historial; }
    
    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    
    // Métodos de negocio
    public boolean validarPin(String pin) {
        return this.pin.equals(pin);
    }
    
    public boolean retirar(double monto) {
        if (saldo >= monto && monto > 0) {
            saldo -= monto;
            registrarTransaccion("RETIRO", monto, saldo);
            return true;
        }
        return false;
    }
    
    public void depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            registrarTransaccion("DEPÓSITO", monto, saldo);
        }
    }
    
    public boolean transferir(Usuario destino, double monto) {
        if (saldo >= monto && monto > 0) {
            saldo -= monto;
            destino.depositar(monto);
            registrarTransaccion("TRANSFERENCIA A " + destino.getDni(), monto, saldo);
            return true;
        }
        return false;
    }
    
    private void registrarTransaccion(String tipo, double monto, double saldoResultante) {
        historial.add(new Transaccion(tipo, monto, saldoResultante, new Date()));
    }
}