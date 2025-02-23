package com.proyecto.inventario.config;

import com.proyecto.inventario.model.Usuario;
import com.proyecto.inventario.service.UsuarioService;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.obtenerPorUsername(username);

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(new SimpleGrantedAuthority(usuario.getRol().name()))
                .build();
    }
}
