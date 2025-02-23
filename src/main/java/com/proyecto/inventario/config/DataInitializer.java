package com.proyecto.inventario.config;

import com.proyecto.inventario.model.Rol;
import com.proyecto.inventario.model.Usuario;
import com.proyecto.inventario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByUsername("piero").isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setUsername("piero");
            usuario.setPassword(passwordEncoder.encode("123456"));
            usuario.setRol(Rol.ADMIN);
            usuario.setNombreCompleto("Piero Tueros");

            usuarioRepository.save(usuario);
            System.out.println("✅ Usuario ADMIN creado con éxito");
        } else {
            System.out.println("ℹ️ Usuario ADMIN ya existe");
        }
    }
}
