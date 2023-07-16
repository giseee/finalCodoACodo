package com.consultas.proyecto.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AvionDTO {

    private String codigo;

    private AerolineaDTO aerolinea;

}
