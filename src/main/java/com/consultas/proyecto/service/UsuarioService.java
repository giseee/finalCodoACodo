package com.consultas.proyecto.service;

import com.consultas.proyecto.dto.UsuarioDTO;
import com.consultas.proyecto.model.Role;
import com.consultas.proyecto.model.Usuario;
import com.consultas.proyecto.repository.RoleRepository;
import com.consultas.proyecto.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.*;



@Service
public class UsuarioService implements UserDetailsService {

	UsuarioRepository usuarioRepository;

	RoleRepository roleRepository;

	PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<Usuario> findById(Long id) {
		return usuarioRepository.findById(id);
	}

	public Optional<Usuario> findByNombre(String nombre) {
		return usuarioRepository.findByNombre(nombre);
	}

	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	public Usuario create(UsuarioDTO usuarioDTO) throws ValidationException {
		
		if (usuarioRepository.findByMail(usuarioDTO.getMail()).isPresent()) {
		      throw new ValidationException("El mail de usuario ya existe!");
		 }
		
		Usuario usuario = new Usuario();
		usuario.setNombre(usuarioDTO.getNombre());
		usuario.setApellido(usuarioDTO.getApellido());
		usuario.setTelefono(usuarioDTO.getTelefono());
		usuario.setMail(usuarioDTO.getMail());
		usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
		usuario.setActivo(true);
		Role perfilEmprendedor = crearPerfilSiNoExiste(Role.ROLE_USER);
		Set<Role> perfilesUsuario = new HashSet<>();
		perfilesUsuario.add(perfilEmprendedor);
		usuario.setRoles(perfilesUsuario);
		return usuarioRepository.save(usuario);
	}
	

	@Transactional
	public boolean update(UsuarioDTO usuarioDTO) {
		return usuarioRepository.findById(usuarioDTO.getId()).map(usuario -> {
			copy(usuarioDTO, usuario);
			return true;
		}).orElse(false);
	}

	private void copy(UsuarioDTO usuarioDTO, Usuario usuario) {
		usuario.setNombre(usuarioDTO.getNombre());
		usuario.setMail(usuarioDTO.getMail());
		Set<Role> perfilesUsuario = new HashSet<>();
		for (String perfil : usuarioDTO.getRoles()) {
			Role perfilEmprendedor = crearPerfilSiNoExiste(perfil);
			perfilesUsuario.add(perfilEmprendedor);
		}		
		usuario.setRoles(perfilesUsuario);
		if(usuarioDTO.getPassword() != null) {
			//es porque cambiaron la contraseña
			usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
		}
	}

	public boolean logicalDelete(Long id) {
		return usuarioRepository.findById(id).map(usuario -> {
			usuario.setActivo(false);
			return true;
		}).orElse(false);
	}

	public boolean activateUser(Long id) {
		return usuarioRepository.findById(id).map(usuario -> {
			usuario.setActivo(true);
			return true;
		}).orElse(false);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> userOptional = usuarioRepository.findByMail(username);
		if (userOptional.isEmpty())
			throw new UsernameNotFoundException("User not found with username: " + username);

		// Creo un usuario(de Spring) que se usará para autenticación y autorización
		// Primero creo los objetos Authority a partir de los perfiles que tiene el
		// usuario.
		Set<Role> perfiles = userOptional.get().getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role perfil : perfiles) {
			authorities.add(new SimpleGrantedAuthority(perfil.getNombreRol()));
		}
		return new User(userOptional.get().getMail(), userOptional.get().getPassword(), authorities);
	}
	
	@Transactional
	Role crearPerfilSiNoExiste(String nombrePerfil) {
		 
		  Optional<Role> perfilOptional = roleRepository.findByNombreRol(nombrePerfil);
	        if (perfilOptional.isEmpty()) {
				Role perfil = new Role();
	            perfil.setNombreRol(nombrePerfil);
	            roleRepository.save(perfil);
	            return perfil;
	        }
	        return perfilOptional.get();
	    }

	public UsuarioDTO loadUserDTO(String mail) {
		Optional<Usuario> userOptional = usuarioRepository.findByMail(mail);
		if (userOptional.isEmpty())
			throw new UsernameNotFoundException("User not found with username: " + mail);
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(userOptional.get().getIdUsuario());
		usuarioDTO.setNombre(userOptional.get().getNombre());
		usuarioDTO.setMail(userOptional.get().getMail());
		Set<String> roles = new HashSet<>();
		for (Role perfil : userOptional.get().getRoles()) {
			roles.add(perfil.getNombreRol());
		}
		usuarioDTO.setRoles(roles);
		return usuarioDTO;
	}
}
