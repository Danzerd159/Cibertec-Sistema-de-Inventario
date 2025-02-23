package com.proyecto.inventario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.inventario.model.Usuario;
import com.proyecto.inventario.repository.UsuarioRepository;

@Service
public class UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario verificarCredenciales(String username, String password) {
        return usuarioRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    
    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public List<Usuario> obtenerListaUsuario(){
    	return usuarioRepository.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }
    
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
