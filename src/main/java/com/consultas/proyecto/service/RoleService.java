package com.consultas.proyecto.service;

import com.consultas.proyecto.dto.RoleDTO;
import com.consultas.proyecto.model.Role;
import com.consultas.proyecto.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class RoleService {
	
	private final RoleRepository roleRepository;
	
	public RoleService (RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public Optional<Role> findById(Long id){
		return roleRepository.findById(id);
	}
	
	public Optional<Role> findByNombreRol(String nombreRol){
		return roleRepository.findByNombreRol(nombreRol);
	}
	
	public List<Role> findAll(){
		return roleRepository.findAll();
	}
	
	public Role create(RoleDTO perfilDTO) {
		Role perfil = new Role();
		perfil.setNombreRol(perfilDTO.getNombreRol());
		return roleRepository.save(perfil);
	}
	
	@Transactional
	public boolean update(Long id, RoleDTO perfilDTO) {
		return roleRepository.findById(id).map(perfil -> {
            copy(perfilDTO, perfil);
            return true;
        })
        .orElse(false);
	}
	
	private void copy(RoleDTO perfilDTO, Role perfil) {
		perfil.setNombreRol(perfilDTO.getNombreRol());
    }
	
	public void delete(Long id) {
		if(roleRepository.existsById(id)) {
			roleRepository.deleteById(id);
		}
	}



}
