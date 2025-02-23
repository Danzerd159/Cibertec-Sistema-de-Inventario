package com.proyecto.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;
}



