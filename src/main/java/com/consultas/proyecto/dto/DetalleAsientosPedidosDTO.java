package com.consultas.proyecto.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DetalleAsientosPedidosDTO {

    @JsonProperty("asiento")
    private AsientoDTO asientoDTO;
    private Long idVuelo;
    private Boolean fueAceptado;
    private String message;



}
