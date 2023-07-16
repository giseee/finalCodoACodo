package com.consultas.proyecto.controller;

import com.consultas.proyecto.dto.Credenciales;
import com.consultas.proyecto.dto.CredencialesRegistro;
import com.consultas.proyecto.dto.UsuarioDTO;
import com.consultas.proyecto.service.UsuarioService;
import com.consultas.proyecto.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@CrossOrigin("*")
@RestController
@RequestMapping(LoginController.AUTH_RESOURCE)
public class LoginController {

	public static final String AUTH_RESOURCE = "/api/public";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody Credenciales credenciales) throws Exception {

		try {

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credenciales.getEmail(), credenciales.getPassword()));

			User user = (User) authentication.getPrincipal();

			final UserDetails userDetails = usuarioService.loadUserByUsername(credenciales.getEmail());

			final String token = jwtTokenUtil.generateToken(userDetails);
			
			UsuarioDTO usuario = usuarioService.loadUserDTO(credenciales.getEmail());

			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(usuario);
		} catch (DisabledException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario deshabilitado");
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o password incorrecto");
		}
	}

	@PostMapping(value = "/register")
	public ResponseEntity<?> register(@RequestBody CredencialesRegistro credencialesRegistro) throws Exception {
		try {
			//refactorizar: sacar el usuario de acá!
			UsuarioDTO usuario = new UsuarioDTO();
			usuario.setMail(credencialesRegistro.getMail());
			usuario.setNombre(credencialesRegistro.getNombre());
			usuario.setApellido(credencialesRegistro.getApellido());
			usuario.setTelefono(credencialesRegistro.getTelefono());
			usuario.setPassword(credencialesRegistro.getPassword());
			return ResponseEntity.ok(usuarioService.create(usuario));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
		
	}

}
