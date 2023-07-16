package com.consultas.proyecto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Reserva")
@Getter
@Setter
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "precio_total", nullable = false)
    private Double precioTotal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_metodo_de_pago", nullable = false)
    private MetodoDePago metodoDePago;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<AsientoVuelo> asientosVuelos;

    public Reserva(Double precioTotal, MetodoDePago metodoDePago, Usuario usuario, List<AsientoVuelo> asientosVuelos) {
        this.precioTotal = precioTotal;
        this.metodoDePago = metodoDePago;
        this.usuario = usuario;
        this.asientosVuelos = asientosVuelos;
    }
}