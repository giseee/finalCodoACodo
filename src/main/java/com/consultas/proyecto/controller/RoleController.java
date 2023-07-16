package com.consultas.proyecto.controller;

import com.consultas.proyecto.dto.RoleDTO;
import com.consultas.proyecto.model.Role;
import com.consultas.proyecto.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(RoleController.PERFIL_RESOURCE)
public class RoleController {

	public static final String PERFIL_RESOURCE = "/api/perfiles";
	
	private final RoleService perfilService;
	
	public RoleController (RoleService perfilService) {
		this.perfilService = perfilService;
	}
	
	@GetMapping
	public List<Role> getAll(){
		return perfilService.findAll();
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<Role> getById(@PathVariable("id") Long id){
		return perfilService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping
    public ResponseEntity<Role> add(@RequestBody @Valid RoleDTO perfilDTO) {
        if(perfilService.findByNombreRol(perfilDTO.getNombreRol()).isPresent()) {
        	return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

		Role perfil = perfilService.create(perfilDTO);
        //TODO quitar el hardcode!!!
        return ResponseEntity.created(URI.create("http://localhost:8080/api/perfiles/"+perfil.getIdRol())).body(perfil);
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid RoleDTO perfilDTO){
		boolean seActualizo = perfilService.update(id, perfilDTO);
		return seActualizo ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if(perfilService.findById(id).isPresent()) {
			perfilService.delete(id);
			return ResponseEntity.noContent().build();
		}		
		return ResponseEntity.notFound().build();
	}			
}