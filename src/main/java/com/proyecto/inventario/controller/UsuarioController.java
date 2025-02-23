package com.proyecto.inventario.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.inventario.model.Rol;
import com.proyecto.inventario.model.Usuario;
import com.proyecto.inventario.service.UsuarioService;

@Controller
public class UsuarioController {
	//Invocamos al Logger
	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;
 
    @GetMapping("/")
    public String mostrarLogin() {
        logger.info("Se accedió a la página de login");
        return "login"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Usuario usuario = usuarioService.obtenerPorUsername(username);
            model.addAttribute("nombre_completo", usuario.getNombreCompleto());

            if (usuario.getRol() == Rol.ADMIN) {
                logger.info("Inicio de sesión exitoso: {} (ADMIN)", username);
                return "redirect:/administrador/index";
            } else if (usuario.getRol() == Rol.EMPLEADOR) {
                logger.info("Inicio de sesión exitoso: {} (EMPLEADOR)", username);
                return "redirect:/empleador/index";
            } else {
                logger.warn("Usuario {} con rol desconocido", username);
                model.addAttribute("error", "Rol desconocido. Contacte al administrador.");
                return "login";
            }

        } catch (Exception e) {
            logger.warn("Intento fallido de inicio de sesión: {}", username);
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }
    

}
