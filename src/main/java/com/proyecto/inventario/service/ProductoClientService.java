package com.proyecto.inventario.service;

import com.proyecto.inventario.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductoClientService {
    private final RestTemplate restTemplate;
    private final String URL_PRODUCTOS = "http://localhost:8081/producto"; // URL del microservicio

    @Autowired
    public ProductoClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //Obtener todos los productos
    public List<Producto> obtenerProductos() {
        Producto[] productos = restTemplate.getForObject(URL_PRODUCTOS, Producto[].class);
        return Arrays.asList(productos);
    }

    //Obtener un producto por ID
    public Producto obtenerProductoPorId(Long id) {
        return restTemplate.getForObject(URL_PRODUCTOS + "/" + id, Producto.class);
    }

    //Buscar producto por nombre
    public Producto obtenerProductoPorNombre(String nombre) {
        return restTemplate.getForObject(URL_PRODUCTOS + "/nombre/" + nombre, Producto.class);
    }

    //Obtener productos ordenados
    public List<Producto> obtenerProductosOrdenados(String campo, boolean ascendente) {
        String url = URL_PRODUCTOS + "/ordenar?campo=" + campo + "&ascendente=" + ascendente;
        Producto[] productos = restTemplate.getForObject(url, Producto[].class);
        return Arrays.asList(productos);
    }

    //Guardar un nuevo producto
    public Producto guardarProducto(Producto producto) {
        return restTemplate.postForObject(URL_PRODUCTOS + "/save", producto, Producto.class);
    }

    public Producto actualizarProducto(Long id, Producto producto) {
        String url = URL_PRODUCTOS + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Producto> request = new HttpEntity<>(producto, headers);

        try {
            ResponseEntity<Producto> response = restTemplate.exchange(url, HttpMethod.PUT, request, Producto.class);
            return response.getBody(); // Devuelve el producto actualizado
        } catch (RestClientException e) {
            // Manejar error de comunicación con el microservicio
            throw new RuntimeException("Error al actualizar el producto con ID: " + id, e);
        }
    }


    //Eliminar un producto
    public void eliminarProducto(Long id) {
        restTemplate.delete(URL_PRODUCTOS + "/" + id);
    }
    
    
}
