package com.consultas.proyecto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Asiento")
@Getter
@Setter
@NoArgsConstructor
public class Asiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento")
    private Long idAsiento;

    @Column(name = "fila", nullable = false)
    private String fila;

    @Column(name = "columna", nullable = false)
    private String columna;

    @Column(name = "esta_apto", nullable = false)
    private Boolean estaApto;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_avion")
    private Avion avion;


    @OneToMany(mappedBy = "asiento", cascade = CascadeType.ALL)
    private List<AsientoVuelo> vuelos;

    public Asiento(String fila, String columna, Boolean estaApto, Avion avion) {
        this.fila = fila;
        this.columna = columna;
        this.estaApto = estaApto;
        this.avion = avion;
    }
}
