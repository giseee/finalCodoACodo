package com.consultas.proyecto.model;

import com.consultas.proyecto.enums.EMetodosDePago;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "MetodoDePago")
@NoArgsConstructor
public class MetodoDePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_de_pago")
    private Long idMetodoDePago;

    @Column(name = "nombre", nullable = false)
    @Enumerated(EnumType.STRING)
    private EMetodosDePago nombreMetodoDePago;

    @Column(name = "descuento", nullable = false)
    private Double descuento;

    @OneToMany(mappedBy = "metodoDePago", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    public MetodoDePago(EMetodosDePago nombreMetodoDePago, Double descuento) {
        this.nombreMetodoDePago = nombreMetodoDePago;
        this.descuento = descuento;
    }

}
