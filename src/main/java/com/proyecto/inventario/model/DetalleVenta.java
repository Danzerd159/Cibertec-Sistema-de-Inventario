package com.proyecto.inventario.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_ventas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @NotNull(message = "El producto es obligatorio")
    @Column(name = "producto_id", nullable = false)
    private Long productoId; // Solo guardamos el ID del producto
    
    @Transient
    private String productoNombre;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a 0")
    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal  precioUnitario;

    @NotNull
    @Positive(message = "El subtotal debe ser mayor a 0")
    @Column(name = "subtotal", nullable = false)
    private BigDecimal  subtotal;

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

}
