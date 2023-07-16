package com.consultas.proyecto.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Vuelo")
@Data
@NoArgsConstructor
public class Vuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vuelo")
    private Long idVuelo;

    @Column(name = "fecha_de_partida", nullable = false)
    private Date fechaDePartida;

    @Column(name = "fecha_de_llegada", nullable = false)
    private Date fechaDeLlegada;

    @Column(name = "origen", nullable = false)
    private String origen;

    @Column(name = "destino", nullable = false)
    private String destino;

    @Column(name = "tiene_conexion", nullable = false)
    private Boolean tieneConexion;

    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL)
    private List<AsientoVuelo> asientosVuelo;



    public Vuelo(Date fechaDePartida, Date fechaDeLlegada, String origen, String destino, Boolean tieneConexion) {
        this.fechaDePartida = fechaDePartida;
        this.fechaDeLlegada = fechaDeLlegada;
        this.origen = origen;
        this.destino = destino;
        this.tieneConexion = tieneConexion;
    }

    public Vuelo(Long idVuelo, Date fechaDePartida, Date fechaDeLlegada, String origen, String destino, Boolean tieneConexion) {
        this.idVuelo = idVuelo;
        this.fechaDePartida = fechaDePartida;
        this.fechaDeLlegada = fechaDeLlegada;
        this.origen = origen;
        this.destino = destino;
        this.tieneConexion = tieneConexion;

    }
}