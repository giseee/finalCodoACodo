package com.consultas.proyecto.dto;

import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VueloDTO {


    private Long idVuelo;

    private Date fechaDePartida;

    private Date fechaDeLlegada;

    private String origen;

    private String destino;

    private Boolean tieneConexion;

    private Long cantidadDeAsientosDisponibles;

    private List<AsientoVueloDTO> asientosVuelo;

    private AvionDTO avion;

}
