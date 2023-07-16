package com.consultas.proyecto.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "avion")
@Getter
@Setter
@NoArgsConstructor
public class Avion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avion")
    private Long idAvion;

    @Column(name = "codigo")
    private String codigo;

    @OneToMany(mappedBy = "avion", cascade = CascadeType.ALL)
    private List<Asiento> asientos;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_aerolinea", nullable = false)
    private Aerolinea aerolinea;

    public Avion(String codigo, Aerolinea aerolinea) {
        this.codigo = codigo;
        this.aerolinea = aerolinea;
    }
}