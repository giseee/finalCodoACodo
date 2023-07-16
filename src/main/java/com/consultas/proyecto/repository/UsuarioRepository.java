package com.consultas.proyecto.repository;

import com.consultas.proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByNombre(String nombre);
	//Optional<Usuario> findByNombreByPasswordByActivo(String nombre, String password, boolean activo);	
	List<Usuario> findAll();
	@Query("SELECT e FROM Usuario e WHERE e.nombre =:name AND e.password= :pass")
	Optional<Usuario> findByNombreByPassword(@Param("name") String nombre, @Param("pass") String password);

	Optional<Usuario> findByMail(String mail);

}