package com.proyecto.inventario.repository;

import com.proyecto.inventario.model.Venta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
	
	@Query("SELECT v FROM Venta v LEFT JOIN FETCH v.detalles WHERE v.id = :id")
	Optional<Venta> findByIdWithDetalles(@Param("id") Long id);

}
