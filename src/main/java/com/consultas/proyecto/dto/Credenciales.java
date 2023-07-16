package com.consultas.proyecto.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Credenciales {

	@NotBlank
	private String email;
	
	@NotBlank
	private String password;

}
