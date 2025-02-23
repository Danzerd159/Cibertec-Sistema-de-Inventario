package com.proyecto.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ventas")
public class Venta {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

    @NotNull(message = "La fecha de la venta no puede ser nula")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El total de la venta es obligatorio")
    @Positive(message = "El total debe ser un número positivo")
    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min = 1, message = "Debe haber al menos un producto en la venta")
    private List<DetalleVenta> detalles;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
