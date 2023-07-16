package com.consultas.proyecto.model;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Rol")
public class Role {

    private static final long serialVersionUID = 1L;
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_SALES_AGENT = "ROLE_SALES_AGENT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long idRol;
    private String nombreRol;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (idPerfil: ");
        result.append(idRol);
        result.append(", nombrePerfil: ");
        result.append(nombreRol);
        result.append(')');
        return result.toString();
    }


}
