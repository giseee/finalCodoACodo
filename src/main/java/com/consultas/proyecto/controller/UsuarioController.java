package com.consultas.proyecto.controller;

import com.consultas.proyecto.dto.UsuarioDTO;
import com.consultas.proyecto.model.Usuario;
import com.consultas.proyecto.service.RoleService;
import com.consultas.proyecto.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(UsuarioController.USUARIO_RESOURCE)
public class UsuarioController {

	public static final String USUARIO_RESOURCE = "/api/usuarios";

	private final UsuarioService usuarioService;

	private final RoleService perfilService;

	public UsuarioController(UsuarioService usuarioService, RoleService perfilService) {
		this.usuarioService = usuarioService;
		this.perfilService = perfilService;
	}

	@GetMapping
	public List<Usuario> getAll() {
		return usuarioService.findAll();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable("id") Long id) {
		return usuarioService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Usuario> add(@RequestBody @Valid UsuarioDTO usuarioDTO) {
		if (usuarioService.findByNombre(usuarioDTO.getNombre()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		Usuario usuario = usuarioService.create(usuarioDTO);
		return ResponseEntity.created(URI.create("http://localhost:8080/api/usuarios/" + usuario.getIdUsuario()))
				.body(usuario);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@RequestBody @Valid UsuarioDTO usuarioDTO) {
		boolean seActualizo = usuarioService.update(usuarioDTO);
		return seActualizo ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> logicalDelete(@PathVariable Long id) {
		boolean seActualizo = usuarioService.logicalDelete(id);
		return seActualizo ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@PutMapping("/activado/{id}")
	public ResponseEntity<Usuario> activateUser(@PathVariable("id") Long id) {
		boolean seActualizo = usuarioService.activateUser(id);
		return seActualizo ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

}