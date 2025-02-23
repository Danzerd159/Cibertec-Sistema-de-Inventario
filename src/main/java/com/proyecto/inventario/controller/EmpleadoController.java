package com.proyecto.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.inventario.model.DetalleVenta;
import com.proyecto.inventario.model.Producto;
import com.proyecto.inventario.model.Usuario;
import com.proyecto.inventario.model.Venta;
import com.proyecto.inventario.service.ProductoClientService;
import com.proyecto.inventario.service.UsuarioService;
import com.proyecto.inventario.service.VentaService;

@Controller
@RequestMapping("/empleador")
public class EmpleadoController {
	
	private final ProductoClientService productoClientService;
	private final UsuarioService usuarioService;
	private final VentaService ventaService;

	@Autowired
	public EmpleadoController(ProductoClientService productoClientService, UsuarioService usuarioService,
			VentaService ventaService) {
		this.productoClientService = productoClientService;
		this.usuarioService = usuarioService;
		this.ventaService = ventaService;
	}

    @GetMapping("/index")
    public String empleadorDashboard(Model model) {
        return "/empleador/index"; 
    }
    
	@GetMapping("/productos")
	public String listarProductos(Model model) {
		List<Producto> productos = productoClientService.obtenerProductos();
		model.addAttribute("productos", productos);
		return "/empleador/productos"; // Vista específica del admin
	}
	
	@GetMapping("/ventas/lista")
	public String listarVentas(Model model) {
		List<Venta> ventas = ventaService.listarVentas();
		model.addAttribute("ventas", ventas);
		return "empleador/ventas/lista"; 
	}
	
	@GetMapping("/ventas/nuevo")
	public String nuevaVenta(Model model) {

		Usuario usuario = usuarioService.obtenerUsuarioPorId(1L);

		List<Producto> productos = productoClientService.obtenerProductos();

		model.addAttribute("usuario", usuario);
		model.addAttribute("productos", productos);

		return "empleador/ventas/nuevo";
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
	    return "empleador/ventas/detalle";
	}
	
	@PostMapping("/ventas/guardar")
	public String guardarVenta(@RequestParam("cantidad[]") List<Integer> cantidades,
	                           @RequestParam("productoId[]") List<Long> productoIds) {

	    Long usuarioId = 3L; 
	    ventaService.guardarVenta(cantidades, productoIds, usuarioId);

	    return "redirect:/empleador/ventas/lista";
	}

}
