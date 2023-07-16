package com.consultas.proyecto.dto;

import com.consultas.proyecto.enums.EMetodosDePago;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ReservaDTO {

    private Long idVuelo;

    private List<AsientoDTO> asientos;

    private EMetodosDePago metodoDePago;

}
