package com.consultas.proyecto.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Asiento_Vuelo")
@Getter
@Setter
@NoArgsConstructor
public class AsientoVuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento")
    private Long idAsiento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_asiento")
    private Asiento asiento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_vuelo")
    private Vuelo vuelo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_reserva")
    private Reserva reserva;

    @Column(name = "esta_libre", nullable = false)
    private Boolean estaLibre;

    @Column(name = "precio_pagado")
    private Double precioPagado;

    @Column(name = "precio")
    private Double precio;

    public AsientoVuelo(Asiento asiento, Vuelo vuelo, Boolean estaLibre, Double precioPagado, Double precio) {
        this.asiento = asiento;
        this.vuelo = vuelo;
        this.estaLibre = estaLibre;
        this.precioPagado = precioPagado;
        this.precio = precio;
    }
}
