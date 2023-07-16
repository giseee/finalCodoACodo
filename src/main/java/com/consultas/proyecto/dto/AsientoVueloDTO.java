package com.consultas.proyecto.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AsientoVueloDTO {


    private AsientoDTO asiento;


    private Boolean estaLibre;


    private Double precio;

}
