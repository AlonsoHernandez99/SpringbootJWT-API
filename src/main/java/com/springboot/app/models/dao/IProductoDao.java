package com.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {

	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String nombre);
	
	public List<Producto> findByNombreLikeIgnoreCase(String nombre);
	
	@Query("select p from Producto p where id = ?1")
	public Producto findProductoById(Long id);
}
