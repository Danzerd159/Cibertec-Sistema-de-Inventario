package com.proyecto.inventario.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 
    @GetMapping("/")
    public String mostrarLogin() {
        logger.info("Se accedió a la página de login");
        return "login"; // Muestra login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        Model model) {
        try {
        	
            Usuario usuario = usuarioService.verificarCredenciales(username, password);
            model.addAttribute("nombre_completo", usuario.getNombreCompleto());
            if (usuario.getRol() == Rol.ADMIN) {
            	logger.info("Inicio de sesión exitoso para el usuario: {} con rol: {}", username, usuario.getRol());
            	return "redirect:/administrador/index";//redireccionar a la vista del administrador
			} else if(usuario.getRol() == Rol.EMPLEADOR){
				logger.info("Inicio de sesión exitoso para el usuario: {} con rol: {}", username, usuario.getRol());
				return "redirect:/empleador/index";//redireccionar a la vista del empleado
			} else {
				logger.warn("Usuario {} tiene un rol desconocido. ", usuario.getUsername());
				model.addAttribute("error", "Usuario con un rol desconodico, pregunte al administrador");
				return "login";
			}
        } catch (RuntimeException e) {
            logger.warn("Intento fallido de inicio de sesión con usuario: {}", username);
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login"; // Si falla, regresa a login
        }
    }
    

}
