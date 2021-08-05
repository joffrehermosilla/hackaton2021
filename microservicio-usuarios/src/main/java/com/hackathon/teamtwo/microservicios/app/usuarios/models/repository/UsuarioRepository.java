package com.hackathon.teamtwo.microservicios.app.usuarios.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hackathon.teamtwo.microservicios.commons.usuarios.models.entity.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>{
	
	@Query("select a from Usuario a where a.nombre like %?1% or a.apellido like %?1%")
	public List<Usuario> findByNombreOrApellido(String term);
	
	@Query(value = "select u.id from usuario u order by id DESC LIMIT 1", nativeQuery = true)
	int lastcode();

}
