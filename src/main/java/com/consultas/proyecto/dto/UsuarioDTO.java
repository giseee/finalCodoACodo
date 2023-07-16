package com.consultas.proyecto.dto;

import lombok.*;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UsuarioDTO {
	
	private Long id;
	
	private String nombre;

	private String apellido;


	private String password;
	
	private String mail;

	private String telefono;


	private boolean activo;
	private Set<String> roles;

}