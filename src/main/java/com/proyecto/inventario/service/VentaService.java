package com.proyecto.inventario.service;

import com.proyecto.inventario.error.VentaNoEncontradaException;
import com.proyecto.inventario.model.DetalleVenta;
import com.proyecto.inventario.model.Producto;
import com.proyecto.inventario.model.Usuario;
import com.proyecto.inventario.model.Venta;
import com.proyecto.inventario.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;	

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioService usuarioService;
    private final ProductoClientService productoClientService;

    @Autowired
    public VentaService(VentaRepository ventaRepository, UsuarioService usuarioService, ProductoClientService productoClientService) {
        this.ventaRepository = ventaRepository;
        this.usuarioService = usuarioService;
        this.productoClientService = productoClientService;
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Venta obtenerVentaConDetalles(Long id) {
        return ventaRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new VentaNoEncontradaException("Venta con ID " + id + " no encontrada"));
    }

    
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    @Transactional
    public Venta guardarVenta(List<Integer> cantidades, List<Long> productoIds, Long usuarioId) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFecha(LocalDate.now());

        List<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal totalVenta = BigDecimal.ZERO;

        for (int i = 0; i < productoIds.size(); i++) {
            Long productoId = productoIds.get(i);
            int cantidad = cantidades.get(i);

            Producto producto = productoClientService.obtenerProductoPorId(productoId);

            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProductoId(productoId);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);
            detalle.setVenta(venta);

            detalles.add(detalle);
            totalVenta = totalVenta.add(subtotal);

            producto.setStock(producto.getStock() - cantidad);

            productoClientService.actualizarProducto(productoId, producto);
        }

        venta.setDetalles(detalles);
        venta.setTotal(totalVenta);
        
        return ventaRepository.save(venta);
    }

}
