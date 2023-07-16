package com.consultas.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="Usuarios")
public class Usuario implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Long idUsuario;
	@Column(name = "nombre", nullable = false )
	private String nombre;

	@Column(name = "apellido", nullable = false )
	private String apellido;

	@Column(name = "telefono", nullable = false )
	private String telefono;
	@Column(name="mail", nullable = false)
	private String mail;

	@JsonIgnore
	@Column(nullable = false)
	private String password;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "usuarios_perfiles",
			joinColumns = @JoinColumn(name = "fk_usuario"),
			inverseJoinColumns = @JoinColumn(name = "fk_rol")
			)
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<Reserva> reservas;
	
	@Column(nullable = false, columnDefinition = "boolean default true")
	private boolean activo;

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (idUsuario: ");
		result.append(idUsuario);
		result.append(", nombre: ");
		result.append(nombre);
		result.append(", mail: ");
		result.append(mail);
		result.append(')');
		return result.toString();
	}
	
}