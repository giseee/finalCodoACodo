package com.consultas.proyecto.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class HistorialReservasDTO {


    private String nombre;

    private String apellido;

    private String mail;

    private String telefono;

    private List<VueloReservaDTO> vuelos;

}
