package com.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

	@Query("SELECT f FROM Factura f JOIN FETCH f.cliente c JOIN FETCH f.detalleFactura d JOIN FETCH d.producto WHERE f.id = ?1 ")
	public Factura fetchByIdWithClienteWithItemFacturaAndProducto(Long id);
}
