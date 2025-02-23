package com.proyecto.inventario.controller;

import com.proyecto.inventario.error.VentaNoEncontradaException;
import com.proyecto.inventario.model.DetalleVenta;
import com.proyecto.inventario.model.Producto;
import com.proyecto.inventario.model.Usuario;
import com.proyecto.inventario.model.Venta;
import com.proyecto.inventario.service.ProductoClientService;
import com.proyecto.inventario.service.UsuarioService;
import com.proyecto.inventario.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

	private final ProductoClientService productoClientService;
	private final UsuarioService usuarioService;
	private final VentaService ventaService;

	@Autowired
	public AdministradorController(ProductoClientService productoClientService, UsuarioService usuarioService,
			VentaService ventaService) {
		this.productoClientService = productoClientService;
		this.usuarioService = usuarioService;
		this.ventaService = ventaService;
	}

	// Página principal del administrador
	@GetMapping("/index")
	public String administradorDashboard(Model model) {
		return "/administrador/index";
	}

	// Listar todos los productos
	@GetMapping("/productos")
	public String listarProductos(Model model) {
		List<Producto> productos = productoClientService.obtenerProductos();
		model.addAttribute("productos", productos);
		return "/administrador/productos"; // Vista específica del admin
	}

	// Página para agregar un nuevo producto
	@GetMapping("/productos/nuevo")
	public String mostrarFormularioNuevoProducto(Model model) {
		model.addAttribute("producto", new Producto()); // Se envía un objeto vacío al formulario
		return "/administrador/formulario-producto";
	}

	// Guardar producto y redirigir a la lista
	@PostMapping("/productos/guardar")
	public String guardarProducto(@ModelAttribute Producto producto) {
		productoClientService.guardarProducto(producto);
		return "redirect:/administrador/productos";
	}

	// Mostrar formulario de edición
	@GetMapping("/editar/{id}")
	public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
		Producto producto = productoClientService.obtenerProductoPorId(id);
		model.addAttribute("producto", producto);
		return "/administrador/producto-editar";
	}

	// Guardar cambios en el producto
	@PostMapping("/actualizar")
	public String actualizarProducto(@ModelAttribute Producto producto) {
		productoClientService.actualizarProducto(producto.getId(), producto);
		return "redirect:/administrador/productos";
	}

	// Eliminar producto con confirmación
	@GetMapping("/eliminar/{id}")
	public String eliminarProducto(@PathVariable Long id) {
		productoClientService.eliminarProducto(id);
		return "redirect:/administrador/productos";
	}

	@GetMapping("/ventas/nuevo")
	public String nuevaVenta(Model model) {

		Usuario usuario = usuarioService.obtenerUsuarioPorId(1L);

		List<Producto> productos = productoClientService.obtenerProductos();

		model.addAttribute("usuario", usuario);
		model.addAttribute("productos", productos);

		return "administrador/ventas/nuevo";
	}

	// Mostrar la lista de todas las ventas
	@GetMapping("/ventas/lista")
	public String listarVentas(Model model) {
		List<Venta> ventas = ventaService.listarVentas();
		model.addAttribute("ventas", ventas);
		return "administrador/ventas/lista"; 
	}
	
	@PostMapping("/ventas/guardar")
	public String guardarVenta(@RequestParam("cantidad[]") List<Integer> cantidades,
	                           @RequestParam("productoId[]") List<Long> productoIds) {

	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    Usuario usuario = usuarioService.obtenerPorUsername(username);
	    ventaService.guardarVenta(cantidades, productoIds, usuario.getId());

	    return "redirect:/administrador/ventas/lista";
	}

	
	@GetMapping("/ventas/detalle/{id}")
	public String detalleVenta(@PathVariable Long id, Model model) {
	    Venta venta = ventaService.obtenerVentaConDetalles(id);

	    // Obtener productos desde el microservicio usando los IDs de los productos en la venta
	    for (DetalleVenta detalle : venta.getDetalles()) {
	        Producto producto = productoClientService.obtenerProductoPorId(detalle.getProductoId());
	        detalle.setProductoNombre(producto.getNombre()); // Guardamos el nombre del producto temporalmente
	    }

	    model.addAttribute("venta", venta);
	    return "administrador/ventas/detalle";
	}
	
    @GetMapping("/usuarios/listado")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerListaUsuario();
        model.addAttribute("usuarios", usuarios);
        return "administrador/usuario/lista";
    }

    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "administrador/usuario/nuevo";
    }
    
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/administrador/usuarios/listado";
    }
    
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        return "administrador/usuario/editar"; 
    }
    
    @PostMapping("/usuarios/actualizar")
    public String actualizarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/administrador/usuarios/listado";
    }
    
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/administrador/usuarios/listado";
    }
}
