package com.consultas.proyecto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Aerolinea")
@Getter
@Setter
public class Aerolinea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_aerolinea")
    private Long idAerolinea;

    @Column(name="nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "aerolinea", cascade = CascadeType.ALL)
    private List<Avion> aviones;

    public Aerolinea(String nombre) {
        this.nombre = nombre;
    }

    public Aerolinea() {
    }
}
