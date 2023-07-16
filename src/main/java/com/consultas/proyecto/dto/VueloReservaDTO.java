package com.consultas.proyecto.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VueloReservaDTO {

    private Long idVuelo;

    private Date fechaDePartida;

    private Date fechaDeLlegada;

    private String origen;

    private String destino;

    private Boolean tieneConexion;

    private AvionDTO avion;

    private String metodoDePago;

}
