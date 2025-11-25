package com.mycompany.cajeroautomatico.servicio;

import com.mycompany.cajeroautomatico.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones bancarias y usuarios
 */
public class BancoService {
    private static BancoService instancia;
    private List<Usuario> usuarios;
    
    private BancoService() {
        usuarios = new ArrayList<>();
        cargarDatosPrueba();
    }
    
    public static BancoService getInstancia() {
        if (instancia == null) {
            instancia = new BancoService();
        }
        return instancia;
    }
    
    private void cargarDatosPrueba() {
        usuarios.add(new Usuario("Juan Pérez", "12345678", "1234", 1500.00));
        usuarios.add(new Usuario("María López", "87654321", "4321", 5000.50));
        usuarios.add(new Usuario("Carlos Ruiz", "11223344", "5678", 3200.75));
    }
    
    public Optional<Usuario> buscarUsuario(String dni) {
        return usuarios.stream()
                .filter(u -> u.getDni().equals(dni))
                .findFirst();
    }
    
    public boolean existeUsuario(String dni) {
        return buscarUsuario(dni).isPresent();
    }
    
    public boolean registrarUsuario(Usuario usuario) {
        if (existeUsuario(usuario.getDni())) {
            return false;
        }
        usuarios.add(usuario);
        return true;
    }
    
    public boolean autenticarUsuario(String dni, String pin) {
        Optional<Usuario> usuario = buscarUsuario(dni);
        return usuario.isPresent() && usuario.get().validarPin(pin);
    }
    
    public List<Usuario> obtenerTodosUsuarios() {
        return new ArrayList<>(usuarios);
    }
}